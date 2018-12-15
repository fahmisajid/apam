package tk.corneliusventi.apam;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class OrderActivity extends AppCompatActivity {

    int summary = 0;
    int quantity = 0;
    int limitApam = 100;
    int apamPrice = 10;
    int addPeanutPrice = 1;
    int addChocolatePrice = 4;
    int addCheesePrice = 2;
    int addBananaPrice = 3;
    boolean isAddPeanut = false;
    boolean isAddChocolate = false;
    boolean isAddCheese= false;
    boolean isAddBanana = false;

    TextView nameTextView;
    TextView quantityTextView;
    TextView summaryTextView;
    CheckBox peanutCheckBox;
    CheckBox chocolateCheckBox;
    CheckBox cheeseCheckBox;
    CheckBox bananaCheckBox;
    Button decrementButton;
    Button incrementButton;
    Button orderButton;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        nameTextView = findViewById(R.id.name);
        peanutCheckBox = findViewById(R.id.peanut);
        chocolateCheckBox = findViewById(R.id.chocolate);
        cheeseCheckBox = findViewById(R.id.cheese);
        bananaCheckBox = findViewById(R.id.banana);
        decrementButton = findViewById(R.id.decrement);
        incrementButton = findViewById(R.id.increment);
        quantityTextView = findViewById(R.id.quantity);
        summaryTextView = findViewById(R.id.summary);
        orderButton = findViewById(R.id.order);

        user = auth.getCurrentUser();
        nameTextView.setText(user.getDisplayName());

        incrementButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity < limitApam) {
                    quantity += 1;
                    changeQuantityView();
                    checkSummary();
                    changeSummaryView();
                }
            }
        });

        decrementButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 0) {
                    quantity -= 1;
                    changeQuantityView();
                    checkSummary();
                    changeSummaryView();
                }
            }
        });

        peanutCheckBox.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAddPeanut = peanutCheckBox.isChecked();
                checkSummary();
                changeSummaryView();

            }
        });
        chocolateCheckBox.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAddChocolate = chocolateCheckBox.isChecked();
                checkSummary();
                changeSummaryView();
            }
        });
        cheeseCheckBox.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAddCheese = cheeseCheckBox.isChecked();
                checkSummary();
                changeSummaryView();
            }
        });
        bananaCheckBox.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAddBanana = bananaCheckBox.isChecked();
                checkSummary();
                changeSummaryView();
            }
        });

        orderButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(quantity > 0) {
                    if(isAddPeanut || isAddChocolate || isAddCheese || isAddBanana) {

                        Map<String, Object> order = new HashMap<>();
                        order.put("name", user.getDisplayName());
                        order.put("quantity", quantity);
                        order.put("peanut", isAddPeanut);
                        order.put("chocolate", isAddChocolate);
                        order.put("cheese", isAddCheese);
                        order.put("banana", isAddBanana);
                        order.put("summary", summary);

                        db.collection("orders")
                            .add(order)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(OrderActivity.this, "Order Successful", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(OrderActivity.this, MainActivity.class));
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(OrderActivity.this, "Order Fail", Toast.LENGTH_LONG).show();
                                }
                            });
                    } else {
                        Toast.makeText(OrderActivity.this, "Minimal One Flavor", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(OrderActivity.this, "Minimal One Apam", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void reset() {
        quantity = 0;
        summary = 0;
        peanutCheckBox.setChecked(false);
        chocolateCheckBox.setChecked(false);
        cheeseCheckBox.setChecked(false);
        bananaCheckBox.setChecked(false);
        isAddPeanut = false;
        isAddChocolate = false;
        isAddCheese = false;
        isAddBanana = false;
        changeQuantityView();
        changeSummaryView();
    }

    protected void changeQuantityView() {
        String quantityText = ""+quantity;
        quantityTextView.setText(quantityText);
    }

    protected void checkSummary() {
        summary = 0;
        int flavor = 0;
        if (isAddPeanut) flavor += addPeanutPrice;
        if (isAddChocolate) flavor += addChocolatePrice;
        if (isAddCheese) flavor += addCheesePrice;
        if (isAddBanana) flavor += addBananaPrice;
        summary += quantity * (apamPrice + flavor);

    }

    protected void changeSummaryView() {
        String sumText = "$"+summary;
        summaryTextView.setText(sumText);
    }
}
