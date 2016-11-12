package com.clpstudio.tvshowtimespent.presentation.detailscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.clpstudio.tvshowtimespent.R;
import com.clpstudio.tvshowtimespent.TvShowApplication;
import com.clpstudio.tvshowtimespent.general.utils.Constants;
import com.clpstudio.tvshowtimespent.general.utils.UrlConstants;
import com.clpstudio.tvshowtimespent.presentation.model.DbTvShow;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    private DbTvShow tvShow;

    @BindView(R.id.image)
    ImageView image;

    @BindView(R.id.title)
    TextView title;

    public static void startActivity(Activity activity, DbTvShow dbTvShow) {
        Intent intent = new Intent(activity, DetailActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.Intents.KEY_TV_SHOW, dbTvShow);
        intent.putExtras(bundle);

        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        ((TvShowApplication) getApplication()).getDiComponent().inject(this);

        tvShow = (DbTvShow) getIntent().getExtras().getSerializable(Constants.Intents.KEY_TV_SHOW);
        setupUi();
    }

    private void setupUi() {
        title.setText(tvShow.getName());

        //download image after size was measured for Picasso center crop size
        ViewTreeObserver vto = image.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                image.getViewTreeObserver().removeOnPreDrawListener(this);
                int finalHeight = image.getMeasuredHeight();
                int finalWidth = image.getMeasuredWidth();

                Picasso.with(DetailActivity.this)
                        .load(UrlConstants.BASE_IMAGE_URL + tvShow.getPosterUrl() + UrlConstants.API_KEY_QUESTION)
                        .resize(finalWidth, finalHeight)
                        .centerCrop()
                        .placeholder(R.color.grey_4_50)
                        .error(R.color.grey_4_50)
                        .into(image);

                return true;
            }
        });
    }
}
