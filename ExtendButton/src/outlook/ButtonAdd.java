package outlook;

import com.example.extendbutton.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ButtonAdd extends Button implements OnClickListener{
	public ButtonAdd(Context context) {
		super(context);
		this.onCreate();
	}
	
	public ButtonAdd(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.onCreate();
	}

	public ButtonAdd(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.onCreate();
	}
	
	private void onCreate() {
		this.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		try {
			View parentView = (View) this.getParent();
			EditText item = (EditText) parentView.findViewById(R.id.editText1);
			Toast.makeText(this.getContext(), "Text: " + item.getText().toString(), Toast.LENGTH_LONG).show();
		} catch (NullPointerException e) {
			System.err.println("Did not work");
		}
	}
}
