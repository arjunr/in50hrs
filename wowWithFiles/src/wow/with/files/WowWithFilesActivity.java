package wow.with.files;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WowWithFilesActivity extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
	
	//Adding shake req variables
	
	private float xPreviousAccel;
	private float yPreviousAccel;
	private float zPreviousAccel;
	private float xAccel;
	private float yAccel;
	private float zAccel;
	private boolean firstUpdate = true;

	/*What acceleration difference would we assume as a rapid movement? */
	private final float shakeThreshold = 3f;
	
	/* Has a shaking motion been started (one direction) */
	private boolean shakeInitiated = false;
	  private SensorManager mySensorManager;
	//End of shake req var
	ImageButton img;
	TextView myText;
	Button buttonOriginal[],lastPressed;
	ArrayList<String> wordList = new ArrayList<String>();
	ArrayList<String> enteredWords = new ArrayList<String>();
	BufferedReader br = null;
	BufferedReader br1 = null;
	String inputWords[],word,firstWord;
	int count=0,score,last[],top=0;
	boolean letter=false,firstW=true;
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Display display=getWindowManager().getDefaultDisplay();
        int w=display.getWidth();
        
        populateList();
        mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE); // (1)
		mySensorManager.registerListener(mySensorEventListener, mySensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL); // (2)
		RelativeLayout myRelativeLayout=(RelativeLayout) findViewById(R.id.name);
		GenerateButton generatebutton=new GenerateButton(word,getApplicationContext(),w);
        buttonOriginal=generatebutton.ButtonCreator();
        last=new int[word.length()+1];
        Score mys=new Score();
        firstWord="";
        score=mys.myScore;
        for(int k=0;k<word.length();k++)
        {	
        	buttonOriginal[k].setOnClickListener(this);
        	Typeface myTypeface = Typeface.createFromAsset(this.getAssets(),"fonts/BABYBLOC.TTF");
        	buttonOriginal[k].setTextSize(30.0f);
        	buttonOriginal[k].setTextColor(Color.RED);
        	buttonOriginal[k].setTypeface(myTypeface);
        	myRelativeLayout.addView(buttonOriginal[k]);
        }
        TextView resu=(TextView) findViewById(R.id.textView1);
        resu.setText("");
        img= new ImageButton(getApplicationContext());
        img.setId(300); 
        
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
  		params.addRule(RelativeLayout.ALIGN_TOP);
  		
  		int c=w/2;
    	int x=c;
    	int f=30;
    	int x1=(x+f/2+w-f/2)/2;
    	double y1=(c*c);
    	y1=x1-c;
  		params.setMargins(c-60,(int)y1+5,0,0 );
  		img.setBackgroundColor(R.drawable.button_shape);
  		img.setLayoutParams(params);
  		img.setImageResource(R.drawable.download);
  		img.setOnClickListener(this);
  		myRelativeLayout.addView(img);
        
  		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
  		
  		myText=new TextView(getApplicationContext());
  		myText.setId(400);
  		params1.setMargins(c-20,(int)(3*y1+10),0,0 );
  		myText.setLayoutParams(params1);
  		myText.setTextSize(30.0f);
    	
  		myRelativeLayout.addView(myText);
  		TextView selectedText= (TextView) findViewById(myText.getId());
        selectedText.setOnClickListener(this);
  		TextView scoreText=(TextView) findViewById(R.id.score);
        scoreText.setText("Score: " +Integer.toString(score));
        new CountDownTimer(121000, 1000) {
        	TextView mTextField=(TextView) findViewById(R.id.timerText);
            public void onTick(long millisUntilFinished) {
            	long millis = millisUntilFinished;
                int seconds = (int) (millis / 1000);
                int minutes = seconds / 60;
                seconds     = seconds % 60;

                if (seconds < 10) 
                {
                   mTextField.setText("" + minutes + ":0" + seconds);
                } 
                else 
                {
                	mTextField.setText("" + minutes + ":" + seconds);            
                }
                
            }

            public void onFinish() {
                mTextField.setText("Done!");
                Intent intent=new Intent(WowWithFilesActivity.this,onEnd.class);
               // intent.putExtra("score", Integer.toString(score));
                Score mySc=new Score();
                mySc.myScore=score;
                if(letter)
                	intent.putExtra("result", "Congrats ! You won !");
                else
                	intent.putExtra("result", "Keep trying !");
                
                startActivity(intent);
                finish();
            }
         }.start();
        setContentView(myRelativeLayout);

	}
	
	//Gesture Functions
	
	 private final SensorEventListener mySensorEventListener = new SensorEventListener() {

		  public void onSensorChanged(SensorEvent se) {
			     /* we will fill this one later */
		        	 updateAccelParameters(se.values[0], se.values[1], se.values[2]);   // (1)
		             if ((!shakeInitiated) && isAccelerationChanged()) {                                      // (2) 
		     	    shakeInitiated = true; 
		     	} else if ((shakeInitiated) && isAccelerationChanged()) {                              // (3)
		     	    executeShakeAction();
		     	} else if ((shakeInitiated) && (!isAccelerationChanged())) {                           // (4)
		     	    shakeInitiated = false;
		     	}
		        	
		        }
		    	private void updateAccelParameters(float xNewAccel, float yNewAccel,
		    			float zNewAccel) {
		                    /* we have to suppress the first change of acceleration, it results from first values being initialized with 0 */
		    		if (firstUpdate) {  
		    			xPreviousAccel = xNewAccel;
		    			yPreviousAccel = yNewAccel;
		    			zPreviousAccel = zNewAccel;
		    			firstUpdate = false;
		    		} else {
		    			xPreviousAccel = xAccel;
		    			yPreviousAccel = yAccel;
		    			zPreviousAccel = zAccel;
		    		}
		    		xAccel = xNewAccel;
		    		yAccel = yNewAccel;
		    		zAccel = zNewAccel;
		    	}
		    	private boolean isAccelerationChanged() {
		    		float deltaX = Math.abs(xPreviousAccel - xAccel);
		    		float deltaY = Math.abs(yPreviousAccel - yAccel);
		    		float deltaZ = Math.abs(zPreviousAccel - zAccel);
		    		return (deltaX > shakeThreshold && deltaY > shakeThreshold)
		    				|| (deltaX > shakeThreshold && deltaZ > shakeThreshold)
		    				|| (deltaY > shakeThreshold && deltaZ > shakeThreshold);
		    	}
		    	   private void executeShakeAction() {
		    			/* Save the cheerleader, save the world 
		    			   or do something more sensible... */
		    		  Shuffle();
		    		}
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
			    /* can be ignored in this example */
			       }
		    };

	//End of Gesture Functions
	
		    
		    
	
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		if(img.getId()==v.getId())
		{
			int index;
			TextView tv=(TextView) findViewById(myText.getId());
			String entered=tv.getText().toString();
			entered=entered.replaceAll(" ", "");
			entered=entered.toLowerCase();
			Log.d("arjun", entered);
			TextView result=(TextView) findViewById(R.id.textView1);
			if(firstWord.equalsIgnoreCase("A")){
				
				Log.d("arjun", "Inside");
				AWords w=new AWords();
				index = Arrays.binarySearch(w.aWords, entered);
				
			}
			else if(firstWord.equalsIgnoreCase("B")){
				
				Log.d("arjun", "Inside");
				BWords w=new BWords();
				index = Arrays.binarySearch(w.bWords, entered);
				
			}
			else if(firstWord.equalsIgnoreCase("C")){
				
				Log.d("arjun", "Inside");
				CWords w=new CWords();
				index = Arrays.binarySearch(w.cWords, entered);
				
			}
			else if(firstWord.equalsIgnoreCase("D")){
				
				Log.d("arjun", "Inside");
				DWords w=new DWords();
				index = Arrays.binarySearch(w.dWords, entered);
				
			}
			else if(firstWord.equalsIgnoreCase("E")){
				
				Log.d("arjun", "Inside");
				EWords w=new EWords();
				index = Arrays.binarySearch(w.eWords, entered);
				
			}
			else if(firstWord.equalsIgnoreCase("F") || firstWord.equalsIgnoreCase("G")){
				
				Log.d("arjun", "Inside");
				FGWords w=new FGWords();
				index = Arrays.binarySearch(w.fgWords, entered);
				
			}
			else if(firstWord.equalsIgnoreCase("H") || firstWord.equalsIgnoreCase("I") || firstWord.equalsIgnoreCase("J") || firstWord.equalsIgnoreCase("K")){
				
				Log.d("arjun", "Inside");
				HIJKWords w=new HIJKWords();
				index = Arrays.binarySearch(w.hijkWords, entered);
				
			}
			else if(firstWord.equalsIgnoreCase("L") || firstWord.equalsIgnoreCase("M")){
				
				LMWords w=new LMWords();
				index = Arrays.binarySearch(w.lmWords, entered);
				Log.d("arjun", "Inside");
			}
			else if(firstWord.equalsIgnoreCase("N") || firstWord.equalsIgnoreCase("O")){
				
				NOWords w=new NOWords();
				index = Arrays.binarySearch(w.noWords, entered);
				Log.d("arjun", "Inside");
			}
			else if(firstWord.equalsIgnoreCase("P") || firstWord.equalsIgnoreCase("Q")){
				
				PQWords w=new PQWords();
				index = Arrays.binarySearch(w.pqWords, entered);
				Log.d("arjun", firstWord);
			}
			else if(firstWord.equalsIgnoreCase("R")){
				
				RWords w=new RWords();
				index = Arrays.binarySearch(w.rWords, entered);
				Log.d("arjun", firstWord);
			}
			else if(firstWord.equalsIgnoreCase("S")){
				
				SWords w=new SWords();
				index = Arrays.binarySearch(w.sWords, entered);
				if(index<0){
					SWords1 sw=new SWords1();
					index = Arrays.binarySearch(sw.sWords, entered);
				}
			}
			else if(firstWord.equalsIgnoreCase("T") || firstWord.equalsIgnoreCase("U")){
				
				TUWords w=new TUWords();
				index = Arrays.binarySearch(w.tuWords, entered);
			}
			else{
				String line;
				String[] vwxyz;
				try {
					br1 = new BufferedReader(new InputStreamReader(getAssets().open("VWXYZWords.txt")));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					while((line=br1.readLine()) != null)
						wordList.add(line);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				vwxyz=wordList.toArray(new String[wordList.size()]);
				index = Arrays.binarySearch(vwxyz, entered);
			}
			
			//index = Arrays.binarySearch(inputWords, entered);
			if(index<0){
				result.setText("Word Not Found !");
				
			}
			else{
				boolean b=enteredWords.contains(entered);
				if(b){
					result.setText("Word already entered !");
				}
				else{
				switch(entered.length()){
				case 3:
					score+=5;
					break;
				case 4:
					score+=10;
					break;
				case 5:
					score+=20;
					break;
				case 6:
					score+=100;
					letter=true;
					
				}
				if(entered.length()<3){
					result.setText(" Word is too small !");
				}
				else{
				enteredWords.add(entered);
				result.setText("    Word found !");
				TextView scoreText=(TextView) findViewById(R.id.score);
				scoreText.setText("Score: " +Integer.toString(score));
				}}
			}
			Handler myHandler = new Handler();
			myHandler.postDelayed(mMyRunnable, 1200);
		}
		else if(myText.getId()==v.getId()){
				
				String textString;
				TextView tapped=(TextView) findViewById(myText.getId());
				textString=tapped.getText().toString();
				if(textString.length()!=0)
				{
				textString=textString.substring(0, textString.length()-2);
				Log.d("arjun", textString);
				buttonOriginal[last[--top]].setClickable(true);
				buttonOriginal[last[top]].setTextColor(Color.RED);
				tapped.setText(textString);
				}
		}
		else
		{
		String text,buttonText;
		TextView tv=(TextView) findViewById(myText.getId());
		for (int i = 0; i < buttonOriginal.length; i++)
		   {
		      if (buttonOriginal[i].getId() == v.getId())
		      {
		    	 buttonOriginal[i].setTextColor(Color.rgb(233, 150, 122));
		    	 buttonOriginal[i].setClickable(false);
		    	 last[top++]=i;
		    	 Log.d("top", Integer.toString(top)+" " + Integer.toString(last[top-1])+" " + buttonOriginal[last[top-1]].getText().toString());
		         buttonText=buttonOriginal[i].getText().toString();
		         text=tv.getText().toString();
		    	 tv.setText(text+" "+buttonText);
		         if(firstW){
		        	 firstWord=buttonText;
		        	 firstW=false;
		        	
		         }
		    	 break;
		      }
		   }
		}
	}
	/*
	@Override
	public void onStart() 
	{
		// TODO Auto-generated method stub
		super.onStart();
	}
	*/
	public void populateList()
	{
		int random1;
        random1=1 + (int)(Math.random() * ((15788 - 0)));
        try {
            br = new BufferedReader(new InputStreamReader(getAssets().open("6letter.txt"))); //throwing a FileNotFoundException?
           
            while(count<=random1)
            {
            	word=br.readLine();
            	count++;
            }
            Log.d("arjun",Integer.toString(count));
            Log.d("arjun",word);
            
        }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            finally 
            {
                try 
                {
                    br.close(); //stop reading
                }
                catch(IOException ex)
                {
                    ex.printStackTrace();
                }
            }
		
	}
	
	public void Reset(){
		top=0;
		for(int i=0;i<word.length();i++){
			
			buttonOriginal[i].setTextColor(Color.RED);
			buttonOriginal[i].setClickable(true);
		}
		TextView tv=(TextView) findViewById(R.id.textView1);
		TextView tv1=(TextView) findViewById(myText.getId());
		tv1.setText("");
		tv.setText("");
		firstW=true;

		
	}
	public void Shuffle(){
		Reset();
		Log.d("arjun", "inside shuffle");
		String wordArray[]=new String[word.length()];
        int number[]=new int[10];
        for(int k=0;k<word.length();k++)
            wordArray[k]=word.substring(k,k+1).toUpperCase();
        for(int j=0;j<word.length();j++)
           number[j]=0;
        int count=0;
        int r=0;
        for(;count<word.length();)
        {
        	r=0 + (int)(Math.random() * ((word.length() - 0)));
            if(number[r]==0)
            {
            	number[r]=1;
            	buttonOriginal[count++].setText(wordArray[r]);
            	Log.d("arjun", Integer.toString(count));
            	Log.d("arjun", wordArray[r]);
            }   
        }
        TextView tv=(TextView) findViewById(R.id.textView1);
		tv.setText("");
	}
	private Runnable mMyRunnable = new Runnable()
	{
	    @Override
	    public void run()
	    {
	    	//Change state here
	    	Reset();
	    }
	 };
	
}