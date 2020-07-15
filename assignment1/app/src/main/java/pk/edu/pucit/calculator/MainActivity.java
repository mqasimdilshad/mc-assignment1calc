package pk.edu.pucit.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity {

    private int[] numericButtons = {R.id.btnZero, R.id.btnOne, R.id.btnTwo, R.id.btnThree, R.id.btnFour, R.id.btnFive, R.id.btnSix, R.id.btnSeven, R.id.btnEight, R.id.btnNine,R.id.doubleZero};

    private int[] operatorButtons = {R.id.btnAdd, R.id.btnSubtract,R.id.btnMultiply, R.id.btnDivide,R.id.modulus};

    private TextView txtScreen;
    private TextView resultScreen;

    private boolean lastNumeric;

    private boolean stateError;

    private boolean lastDot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.txtScreen = (TextView) findViewById(R.id.tv_equation);
        this.resultScreen=(TextView)findViewById(R.id.tv_result);

        setNumericOnClickListener();
        setOperatorOnClickListener();

    }

    private void setNumericOnClickListener() {

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView button = (TextView) v;
                if (stateError) {

                    txtScreen.setText(button.getText());
                    stateError = false;
                } else {

                    txtScreen.append(button.getText());
                }

                lastNumeric = true;
            }
        };

        for (int id : numericButtons) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    private void setOperatorOnClickListener() {
        // Create a common OnClickListener for operators
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastNumeric && !stateError) {
                    TextView button = (TextView) v;
                    txtScreen.append(button.getText());
                    lastNumeric = false;
                    lastDot = false;
                }
            }
        };
        for (int id : operatorButtons) {
            findViewById(id).setOnClickListener(listener);
        }

        findViewById(R.id.dot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastNumeric && !stateError && !lastDot) {
                    txtScreen.append(".");
                    lastNumeric = false;
                    lastDot = true;
                }
            }
        });

        findViewById(R.id.ac).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtScreen.setText("");

                lastNumeric = false;
                stateError = false;
                lastDot = false;
            }
        });

        findViewById(R.id.equal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEqual();
            }
        });


        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eq=txtScreen.getText().toString();
                if(eq!=null && eq.length()>0)
                {
                    if(eq.length()==1)
                    {
                        txtScreen.setText("");
                    }
                    else{
                        eq=eq.substring(0,eq.length()-1);
                        txtScreen.setText(eq);
                    }
                }
            }
        });

    }


    private void onEqual() {
        if (lastNumeric && !stateError) {

            String txt = txtScreen.getText().toString();

            Expression expression = new ExpressionBuilder(txt).build();
            try {

                double result = expression.evaluate();
               resultScreen.setText(Double.toString(result));
                lastDot = true; // Result contains a dot
            } catch (ArithmeticException ex) {

                resultScreen.setText("Error");
                stateError = true;
                lastNumeric = false;
            }
        }
    }


}
