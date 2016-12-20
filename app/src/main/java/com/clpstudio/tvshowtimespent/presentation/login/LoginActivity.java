package com.clpstudio.tvshowtimespent.presentation.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.clpstudio.tvshowtimespent.R;
import com.clpstudio.tvshowtimespent.TvShowApplication;
import com.clpstudio.tvshowtimespent.presentation.mainscreen.MainActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements ILoginPresenter.View {

    @Inject
    ILoginPresenter presenter;

    @BindView(R.id.username)
    EditText usernameEditText;

    @BindView(R.id.password)
    EditText passwordEditText;

    @BindView(R.id.progress_bar)
    View progressBar;

    @BindView(R.id.content)
    View mainContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ((TvShowApplication) getApplicationContext()).getDiComponent().inject(this);
        ButterKnife.bind(this);
        presenter.bindView(this);
    }

    @OnClick(R.id.login)
    public void onLoginClick() {
        presenter.login(usernameEditText.getText().toString(), passwordEditText.getText().toString());
    }

    @Override
    public void showError() {
        Toast.makeText(this, "Wrong username or password!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void gotoMainPage() {
        MainActivity.startActivity(this);
        finish();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        mainContent.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        mainContent.setVisibility(View.VISIBLE);
    }
}
