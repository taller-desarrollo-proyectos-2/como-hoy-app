package com.fiuba.gaff.comohoy;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.fiuba.gaff.comohoy.model.purchases.Address;
import com.fiuba.gaff.comohoy.model.purchases.CreditCardDetails;
import com.fiuba.gaff.comohoy.model.purchases.PaymentDetails;
import com.fiuba.gaff.comohoy.model.purchases.PaymentMethod;
import com.fiuba.gaff.comohoy.services.PurchasesService.OnSubmitPurchaseCallback;
import com.fiuba.gaff.comohoy.services.PurchasesService.PurchasesService;
import com.fiuba.gaff.comohoy.services.ServiceLocator;

public class ConfirmPurchaseActivity extends AppCompatActivity {

    private Dialog mSelectAddressDialog;
    private Dialog mAdditionalInfoDialog;
    private Dialog mCreditCardDialog;

    private Address mShippingAddress = new Address();

    private PaymentMethod mPaymentMethod = PaymentMethod.Cash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_purchase);

        createDialogs();

        setUpCardViews();
        setUpPaymentMethodSpinner();
        updateCreditCardViewVisibility();
        setUpCancelButton();
        setUpConfirmPurchaseButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCreditCardDetailsValue();
    }

    private void setUpCancelButton() {
        Button cancelButton = findViewById(R.id.button_cancel_purchase);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setUpConfirmPurchaseButton() {
        Button acceptButton = findViewById(R.id.button_confirm_purchase);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePaymentDetailsValues();
                if (isFormValid()) {
                    submitPurchase();
                }
            }
        });
    }

    private boolean isFormValid() {
        return (isAddressFormValid() && isCreditCardDetailsValid());
    }

    private boolean isAddressFormValid() {
        CardView addressCardView = findViewById(R.id.cardview_address);
        if ((mShippingAddress.getStreetName().isEmpty()) || (mShippingAddress.getStreetNumber().isEmpty())){
            addressCardView.setBackgroundColor(getResources().getColor(R.color.light_red));
            return false;
        }
        return true;
    }

    private boolean isCreditCardDetailsValid() {
        CardView creditCardCardView = findViewById(R.id.cardview_card_details);
        PaymentDetails paymentDetails = getPurchaseService().getPaymentDetails();
        boolean payWithCreditCard = paymentDetails.getPaymentMethod().equals(PaymentMethod.CreditCard);
        boolean emptyCardValues = paymentDetails.getCardDetails() == null;
        if (payWithCreditCard && emptyCardValues) {
            creditCardCardView.setBackgroundColor(getResources().getColor(R.color.light_red));
            return  false;
        }
        creditCardCardView.setBackgroundColor(getResources().getColor(R.color.blanco));
        return true;
    }

    private void createDialogs() {
        createSelectAddressDialog();
        createAddressAdditionalInfoDialog();
        createCreditCardDialog();
    }

    private void setUpCardViews() {
        setUpSelectAddressCardView();
        setUpAdditionalInfoCardView();
        setUpCreditCardCardView();
    }

    private void setUpPaymentMethodSpinner() {
        Spinner spinner = findViewById(R.id.spinner_payment_method);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.payment_methods_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                String selectedItem = adapterView.getItemAtPosition(pos).toString();
                mPaymentMethod = PaymentMethod.fromString(selectedItem);
                updateCreditCardViewVisibility();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setUpSelectAddressCardView() {
        CardView addressCardView = findViewById(R.id.cardview_address);
        addressCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectAddressDialog();
            }
        });

        updateAddressValue();
    }

    private void setUpAdditionalInfoCardView() {
        createAddressAdditionalInfoDialog();
        CardView additionalInfoCardView = findViewById(R.id.cardview_address_additional_info);
        additionalInfoCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAdditionalInfoDialog();
            }
        });
        updateAdditionalInfoValue();
    }

    private void setUpCreditCardCardView() {
        CardView creditCardCardView = findViewById(R.id.cardview_card_details);
        creditCardCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreditCardActivity();
            }
        });
    }

    private void openCreditCardActivity() {
        Intent intent = new Intent(this, CardDetailsActivity.class);
        startActivity(intent);
    }

    private void createSelectAddressDialog() {
        mSelectAddressDialog = new Dialog(this);
        mSelectAddressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSelectAddressDialog.setContentView(R.layout.dialog_address);
        mSelectAddressDialog.setCanceledOnTouchOutside(false);
        mSelectAddressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mSelectAddressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final TextInputLayout streetInputLayout = mSelectAddressDialog.findViewById(R.id.textview_address_street);
        final EditText streetEditText = mSelectAddressDialog.findViewById(R.id.edittext_address_street_value);
        final TextInputLayout streetNumberInputLayout = mSelectAddressDialog.findViewById(R.id.textview_address_number);
        final EditText numberEditText = mSelectAddressDialog.findViewById(R.id.edittext_address_number_value);

        Button acceptButton = mSelectAddressDialog.findViewById(R.id.button_accept);
        Button cancelButton = mSelectAddressDialog.findViewById(R.id.button_cancel);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validInput = true;
                String street = streetEditText.getText().toString();
                String streetNumber = numberEditText.getText().toString();
                if (street.isEmpty()) {
                    streetInputLayout.setError("Ingrese una calle");
                    validInput = false;
                }
                if (streetNumber.isEmpty()) {
                    streetNumberInputLayout.setError("Ingrese un número de calle");
                    validInput = false;
                }

                if (validInput) {
                    CardView creditCardCardView = findViewById(R.id.cardview_address);
                    creditCardCardView.setBackgroundColor(getResources().getColor(R.color.blanco));
                    mShippingAddress.setStreetName(street);
                    mShippingAddress.setStreetNumber(streetNumber);
                    updateAddressValue();
                    mSelectAddressDialog.dismiss();
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectAddressDialog.dismiss();
            }
        });
    }

    private void createAddressAdditionalInfoDialog() {
        mAdditionalInfoDialog = new Dialog(this, android.R.style.Theme_Holo_Light_Dialog);
        mAdditionalInfoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mAdditionalInfoDialog.setContentView(R.layout.dialog_clarifications);
        mAdditionalInfoDialog.setCanceledOnTouchOutside(false);
        mAdditionalInfoDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mAdditionalInfoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mAdditionalInfoDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        TextView title = mAdditionalInfoDialog.findViewById(R.id.textView_title);
        title.setText("Información Adicional");
        final EditText additionalInfoEditText = mAdditionalInfoDialog.findViewById(R.id.editText_clarifications);

        Button acceptButton = mAdditionalInfoDialog.findViewById(R.id.button_accept);
        Button cancelButton = mAdditionalInfoDialog.findViewById(R.id.button_cancel);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShippingAddress.setAdditionalInformation(additionalInfoEditText.getText().toString());
                updateAdditionalInfoValue();
                mAdditionalInfoDialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdditionalInfoDialog.dismiss();
            }
        });
    }

    private void createCreditCardDialog() {
        mCreditCardDialog = new Dialog(this, android.R.style.Theme_Holo_Light_Dialog);
        mCreditCardDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mCreditCardDialog.setContentView(R.layout.dialog_credit_card);
        mCreditCardDialog.setCanceledOnTouchOutside(false);
        mCreditCardDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mCreditCardDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mCreditCardDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        CardForm cardForm = mCreditCardDialog.findViewById(R.id.card_form);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("SMS is required on this number")
                .actionLabel("Purchase")
                .setup(this);

        Button acceptButton = mCreditCardDialog.findViewById(R.id.button_accept);
        Button cancelButton = mCreditCardDialog.findViewById(R.id.button_cancel);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCreditCardDialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCreditCardDialog.dismiss();
            }
        });
    }

    private void updateAddressValue() {
        TextView addressTextView = findViewById(R.id.address_field_value);
        String shownValue = String.format("%s %s", mShippingAddress.getStreetName(), mShippingAddress.getStreetNumber());
        addressTextView.setText(shownValue);
    }

    private void updateAdditionalInfoValue() {
        TextView additionalInfoTextView = findViewById(R.id.address_field_additional_info_value);
        additionalInfoTextView.setText(mShippingAddress.getAdditionalInformation());
    }

    private void updateCreditCardDetailsValue() {
        CreditCardDetails cardDetails = getPurchaseService().getPaymentDetails().getCardDetails();
        if (cardDetails != null) {
            CardView creditCardCardView = findViewById(R.id.cardview_card_details);
            creditCardCardView.setBackgroundColor(getResources().getColor(R.color.blanco));
            String cardNumber = cardDetails.getCardNumber();
            String hiddenCardNumber = "";
            if (cardNumber.length() > 4) {
                String last4Digits = cardNumber.substring(cardNumber.length() - 4);
                hiddenCardNumber = String.format("********%s", last4Digits);
            }
            TextView creditCardTextView = findViewById(R.id.payment_card_details);
            creditCardTextView.setText(hiddenCardNumber);
        }
    }

    private void showSelectAddressDialog() {
        final TextInputLayout streetInputLayout = mSelectAddressDialog.findViewById(R.id.textview_address_street);
        final EditText streetEditText = mSelectAddressDialog.findViewById(R.id.edittext_address_street_value);
        final TextInputLayout streetNumberInputLayout = mSelectAddressDialog.findViewById(R.id.textview_address_number);
        final EditText numberEditText = mSelectAddressDialog.findViewById(R.id.edittext_address_number_value);

        streetInputLayout.setError("");
        streetNumberInputLayout.setError("");
        streetEditText.setText(mShippingAddress.getStreetName());
        numberEditText.setText(mShippingAddress.getStreetNumber());
        mSelectAddressDialog.show();
    }

    private void showAdditionalInfoDialog() {
        EditText additionalInfoEditText = mAdditionalInfoDialog.findViewById(R.id.editText_clarifications);
        additionalInfoEditText.setText(mShippingAddress.getAdditionalInformation());
        additionalInfoEditText.requestFocus();
        mAdditionalInfoDialog.show();
    }

    private void updateCreditCardViewVisibility() {
        int visibility = View.GONE;
        switch (mPaymentMethod) {
            case Cash:
                visibility = View.GONE;
                break;
            case CreditCard:
                visibility = View.VISIBLE;
                break;
        }
        CardView creditCardCardView = findViewById(R.id.cardview_card_details);
        creditCardCardView.setVisibility(visibility);
    }

    private void updatePaymentDetailsValues() {
        PurchasesService purchasesService = getPurchaseService();
        PaymentDetails paymentDetails = purchasesService.getPaymentDetails();

        double amountToCharge = purchasesService.getCart().getTotalPrice();
        paymentDetails.setAmountToCharge(amountToCharge);
        paymentDetails.setPaymentMethod(mPaymentMethod);
        paymentDetails.setShippingAddress(mShippingAddress);
        purchasesService.setPaymentDetails(paymentDetails);
    }

    private void submitPurchase() {
        getPurchaseService().submitPurchase(this, new OnSubmitPurchaseCallback() {
            @Override
            public void onSuccess() {
                openOKMessage();
                goBackToCommercesActivity();
            }

            @Override
            public void onError(String reason) {
                showOrderSubmitError(reason);
            }
        });
    }

    private void goBackToCommercesActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void showOrderSubmitError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private PurchasesService getPurchaseService() {
        return ServiceLocator.get(PurchasesService.class);
    }

    private void openOKMessage(){
        mCreditCardDialog = new Dialog(this, android.R.style.Theme_Holo_Light_Dialog);
        mCreditCardDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mCreditCardDialog.setContentView(R.layout.dialog_notificacion_purchase_ok);
        mCreditCardDialog.setCanceledOnTouchOutside(false);
        mCreditCardDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mCreditCardDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mCreditCardDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        Button okButton = mCreditCardDialog.findViewById(R.id.button_accept_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCreditCardDialog.dismiss();
            }
        });
        mCreditCardDialog.show();
    }
}
