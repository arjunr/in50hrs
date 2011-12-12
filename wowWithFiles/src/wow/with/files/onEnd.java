package wow.with.files;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class onEnd extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onend);
		
		TextView res=(TextView) findViewById(R.id.score);
	//	res.setText(getIntent().getExtras().getString("score"));
		Score my=new Score();
		res.setText(Integer.toString(my.myScore));
		TextView ans=(TextView) findViewById(R.id.result);
		ans.setText(getIntent().getExtras().getString("result"));
		String r=getIntent().getExtras().getString("result");
		if(r.equalsIgnoreCase("Congrats ! You won !"))
		{}
		else
			my.myScore=0;
		Button bt=(Button) findViewById(R.id.button1);
		bt.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.button1){
			Intent intent=new Intent(onEnd.this,WowWithFilesActivity.class);
			startActivity(intent);
			finish();
		}
	}
}
