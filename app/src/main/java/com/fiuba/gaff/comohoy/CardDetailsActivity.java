package com.fiuba.gaff.comohoy;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.braintreepayments.cardform.OnCardFormSubmitListener;
import com.braintreepayments.cardform.view.CardForm;
import com.fiuba.gaff.comohoy.model.purchases.CreditCardDetails;
import com.fiuba.gaff.comohoy.model.purchases.PaymentDetails;
import com.fiuba.gaff.comohoy.services.PurchasesService.PurchasesService;
import com.fiuba.gaff.comohoy.services.ServiceLocator;

public class CardDetailsActivity extends AppCompatActivity {

    private CardForm mCardForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_form);

        mCardForm = findViewById(R.id.card_form);

        setUpCardForm();
        setUpAcceptButton();
        setUpCancelButton();
    }

    private void setUpCardForm() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .actionLabel("Aceptar")
                .setup(this);

        mCardForm.setOnCardFormSubmitListener(new OnCardFormSubmitListener() {
            @Override
            public void onCardFormSubmit() {
                tryToSubmitCardValues();
            }
        });
    }

    private void setUpAcceptButton() {
        Button acceptButton = findViewById(R.id.button_accept);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryToSubmitCardValues();
            }
        });
    }

    private void tryToSubmitCardValues() {
        if (isValid()) {
            setCardValues();
            finish();
        } else {
            showToast("Complete todos los campos");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG);
    }

    private boolean isValid() {
        mCardForm.validate();
        boolean formValid = mCardForm.isValid();
        boolean nameValid = isNameFieldValid();
        return formValid && nameValid;
    }

    private boolean isNameFieldValid() {
        TextInputLayout textInputLayout = findViewById(R.id.layout_card_owner);
        EditText nameEditText = findViewById(R.id.edittext_address_street_value);
        if (nameEditText.getText().toString().isEmpty()) {
            textInputLayout.setError("El nombre es obligatorio");
            return false;
        }
        return true;
    }

    private void setUpCancelButton() {
        Button cancelButton = findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setCardValues() {
        PurchasesService purchasesService = getPurchaseService();
        CardForm cardForm = findViewById(R.id.card_form);
        EditText nameEditText = findViewById(R.id.edittext_address_street_value);

        PaymentDetails paymentDetails = purchasesService.getPaymentDetails();
        CreditCardDetails cardDetails = new CreditCardDetails();

        String name = nameEditText.getText().toString();
        String cardNumber = cardForm.getCardNumber();
        String cvv = cardForm.getCvv();
        String expirationDate = String.format("%s-%s", cardForm.getExpirationMonth(), cardForm.getExpirationYear());

        cardDetails.setOwnerName(name);
        cardDetails.setCardNumber(cardNumber);
        cardDetails.setSecurityNumber(cvv);
        cardDetails.setExpireDate(expirationDate);

        paymentDetails.setCardDetails(cardDetails);
        purchasesService.setPaymentDetails(paymentDetails);
    }

    private PurchasesService getPurchaseService() {
        return ServiceLocator.get(PurchasesService.class);
    }
}
