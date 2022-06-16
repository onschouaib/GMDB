package com.dam.gmdb;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dam.gmdb.commons.Utils;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;

import java.util.Arrays;
import java.util.List;
import com.dam.gmdb.commons.Utils.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    /** Vars globales **/
    private View baseView;
    public String message;


    /** Méthode initUI pour les composants du design **/
    private void initUI(){
        baseView = findViewById(R.id.mainLayoutSignIn);
    }

    /** La variable du callback avec le retour du type de signLauncher **/

    private final ActivityResultLauncher<Intent> signLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignResult(result);
                }
            }
    );

    //** La methode pour recuperer le callback de la cnx **//
    private void onSignResult(FirebaseAuthUIAuthenticationResult result){
        IdpResponse respone = result.getIdpResponse();

        if (result.getResultCode() == RESULT_OK){
            // Connecté !!
            message = getString(R.string.connected);
        }else {
            //Pas connecté
            if(respone == null){
                message = getString(R.string.error);

            } else if(respone.getError().getErrorCode() == ErrorCodes.NO_NETWORK){
                message = getString(R.string.nointernet);

            } else if (respone.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR){
                message = getString(R.string.unknownerror);
            }
        }
        Utils.showSnackBar(baseView, message);
    }
    /** Méthode de gestion du SignUP **/
    private void signUpActivity(){
        List<AuthUI.IdpConfig> providers = Arrays.asList(
          new AuthUI.IdpConfig.EmailBuilder().build(),
          new AuthUI.IdpConfig.PhoneBuilder().build(),
          new AuthUI.IdpConfig.GoogleBuilder().build(),
          new AuthUI.IdpConfig.GitHubBuilder().build()
            );

        Intent signUpIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                //Ajout le théme et le logo
                .setLogo(R.drawable.logo)
                .setTheme(R.style.LoginTheme)
                //RGBD
                .setTosAndPrivacyPolicyUrls("httpps://google.com", "https://yahoo.com")
                //Enregistre l'état de l'utilisateur ( connected or not, that is the question ?)
                .setIsSmartLockEnabled(true)
                .build();

        signLauncher.launch(signUpIntent);


    }

    public void startSignUpActivity(View view){
        signUpActivity();
    }
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!=null){
            startActivity(new Intent(SignInActivity.this, HomeActivity.class));
        }

    }
}