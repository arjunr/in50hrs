package wow.post.hrs50;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class onEnd extends Activity implements OnClickListener{

	@Override
	public void onCreate(Bundle savedInstanceState){
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
			else{
				my.myScore=0;
				TextView re=(TextView) findViewById(R.id.result);
				re.setText("The word was : "+wordClass.word);
			}
			Button bt=(Button) findViewById(R.id.button1);
			bt.setOnClickListener(this);
			TextView message=(TextView) findViewById(R.id.Message);
			message.setTextColor(Color.BLACK);
			TextView result=(TextView) findViewById(R.id.result);
			result.setTextColor(Color.BLACK);
			TextView tv1=(TextView) findViewById(R.id.textView1);
			tv1.setTextColor(Color.BLACK);
			TextView sc=(TextView) findViewById(R.id.score);
			sc.setTextColor(Color.BLACK);
			RelativeLayout myRelativeLayout=(RelativeLayout) findViewById(R.id.end);
			myRelativeLayout.setBackgroundColor(Color.WHITE);
		
	}
	
	@Override
	public void onBackPressed() {
		   android.os.Process.killProcess(android.os.Process.myPid()) ;
		finish();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.button1){
			Intent intent=new Intent(onEnd.this,WowPost50Activity.class);
			startActivity(intent);
			finish();
		}
	}
}
