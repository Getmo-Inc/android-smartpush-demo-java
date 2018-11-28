package br.com.getmo.smartpushshowcase.tag;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import br.com.getmo.smartpushshowcase.R;
import br.com.getmo.smartpushshowcase.SharePreferencesUtil;
import br.com.smartpush.Smartpush;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentTags extends Fragment {

    @BindView( R.id.tag_news_feed_politica )
    CheckBox checkBoxPolitica;

    @BindView( R.id.tag_news_feed_economia )
    CheckBox checkBoxEconomia;

    @BindView( R.id.tag_news_feed_esportes )
    CheckBox checkBoxEsportes;

    @Nullable
    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate( R.layout.fragment_tags, container, false );
    }

    @Override
    public void onViewCreated( @NonNull View view, @Nullable Bundle savedInstanceState ) {
        super.onViewCreated( view, savedInstanceState );

        ButterKnife.bind(this, view );

        refreshUI();

    }

    @OnClick( { R.id.tag_news_feed_politica, R.id.tag_news_feed_economia, R.id.tag_news_feed_esportes, R.id.btn_reset } )
    public void onClick( View view ) {

        if ( view.getId() == R.id.tag_news_feed_politica ) {
            addOrRemoveTagValue(
                    getContext(), ( ( CheckBox ) view ).isChecked(), "NEWS_FEED", "POLITICA" );
        } else if ( view.getId() == R.id.tag_news_feed_economia ) {
            addOrRemoveTagValue(
                    getContext(), ( ( CheckBox ) view ).isChecked(), "NEWS_FEED", "ECONOMIA" );
        } else if ( view.getId() == R.id.tag_news_feed_esportes ) {
            addOrRemoveTagValue(
                    getContext(), ( ( CheckBox ) view ).isChecked(), "NEWS_FEED", "ESPORTES" );
        } else if ( view.getId() == R.id.btn_reset ) {
            Smartpush.delTagOrValue( getContext(), "NEWS_FEED", ( ArrayList ) null );

            resetLocalTagValues();

            refreshUI();
        }
    }

    /**
     * Neste exemplo foi utilizada uma TAG do tipo LIST.
     * As TAGs devem ser criadas primeiramente no painel SMARTPUSH antes que possam ser utilizadas.
     * https://admin.getmo.com.br/tags
     *
     * Os tipos de TAGs são: LIST, STRING, DATE, NUMBER, e BOOLEAN.
     */

    private void addOrRemoveTagValue( Context context, boolean isChecked, String name, String value ) {
        ArrayList<String> values = new ArrayList<>( );
        values.add( value );

        if ( isChecked ) {
            Smartpush.setTag( context, name, values );
            SharePreferencesUtil.set( context, name + "." + value, true );
        } else {
            Smartpush.delTagOrValue( context, name, values );
            SharePreferencesUtil.remove( context, name + "." + value );
        }
    }

    private void refreshUI() {
        checkBoxPolitica.setChecked(
                SharePreferencesUtil.getBoolean( getContext(), "NEWS_FEED.POLITICA" ) );

        checkBoxEconomia.setChecked(
                SharePreferencesUtil.getBoolean( getContext(), "NEWS_FEED.ECONOMIA" ) );

        checkBoxEsportes.setChecked(
                SharePreferencesUtil.getBoolean( getContext(), "NEWS_FEED.ESPORTES" ) );
    }

    private void resetLocalTagValues() {
        SharePreferencesUtil.remove( getContext(), "NEWS_FEED.POLITICA" );
        SharePreferencesUtil.remove( getContext(), "NEWS_FEED.ECONOMIA" );
        SharePreferencesUtil.remove( getContext(), "NEWS_FEED.ESPORTES" );
    }
}