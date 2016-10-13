package pptik.org.mobildereklocator;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import net.qiujuer.genius.blur.StackBlur;

public class tes extends AppCompatActivity {

    View view1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bitmap bg= BitmapFactory.decodeResource(this.getResources(),
                R.drawable.tes1);
        bg=StackBlur.blurNativelyPixels(bg,8,false);
        view1=(View)findViewById(R.id.opacityFilter);
        view1.setBackground(new BitmapDrawable(bg));

    }

}
