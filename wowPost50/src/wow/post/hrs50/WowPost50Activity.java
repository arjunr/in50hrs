package wow.post.hrs50;

import java.io.BufferedReader;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WowPost50Activity extends Activity implements OnClickListener{
    
	String word="",pressedButton="";
	BufferedReader br = null;
	Button buttonOriginal[],lastPressed,img;
	Context myContext;
	TextView timerText,myText,result,wordsEntered[],suggestions[];
	int last[],top=0,score=0,buttonCount=0,buttonPressed[];
	ArrayList<String> enteredWords = new ArrayList<String>();
	/** Called when the activity is first created. */
	Boolean letter=false;
	
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
   
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
   
        myContext=getApplicationContext();
        GenerateWord wordGen=new GenerateWord();
        word=wordGen.wordGenerator(myContext);
        temp p = new temp();
        new Thread(p).start();
        
        RelativeLayout myRelativeLayout=(RelativeLayout) findViewById(R.id.name);
        Display display=getWindowManager().getDefaultDisplay();
        int screenWidth=display.getWidth();
        GenerateButton generatebutton=new GenerateButton(word,getApplicationContext(),screenWidth);
        Log.d("arjun", "Button Has been gen");
        buttonOriginal=generatebutton.ButtonCreator();
        last=new int[word.length()+1];
        buttonPressed=new int[word.length()];
        for(int k=0;k<word.length();k++)
        {	
        	buttonOriginal[k].setOnClickListener(this);
        	buttonOriginal[k].setTextSize(35.0f);
        	Typeface myTypeface = Typeface.createFromAsset(this.getAssets(),"fonts/batmfa.ttf");
        	buttonOriginal[k].setTextColor(Color.BLACK);
        	buttonPressed[k]=0;
        	buttonOriginal[k].setTypeface(myTypeface);
        	myRelativeLayout.addView(buttonOriginal[k]);
        }
     
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        params1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        timerText=new TextView(getApplicationContext());
  		timerText.setId(500);
  		timerText.setLayoutParams(params1);
  		timerText.setTextSize(20.0f);
  		Typeface myTypeface = Typeface.createFromAsset(this.getAssets(),"fonts/Futured.TTF");
  		timerText.setTextColor(Color.BLACK);
  		timerText.setTypeface(myTypeface);
    	myRelativeLayout.addView(timerText);
    	
    	
    	
        new CountDownTimer(121000, 1000) {
        	
        	TextView mTextField=(TextView) findViewById(timerText.getId());
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
                mTextField.setText("Time's UP!");
                
                Intent intent=new Intent(WowPost50Activity.this,onEnd.class);
                intent.putExtra("score", Integer.toString(score));
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

       
         
        int c=screenWidth/2;
     	int x=c;
     	int f=30;
     	int x1=(x+f/2+screenWidth-f/2)/2;
     	double y1=(c*c);
     	y1=x1-c;
     	
     	img=new Button(getApplicationContext());
     	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
  		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
  		params.setMargins(0,(int)(y1+15),0,0 );
  		img.setBackgroundColor(R.drawable.button_shape);
  		img.setLayoutParams(params);
  		myRelativeLayout.addView(img);
  		img.setTextSize(25.0f);
  		img.setBackgroundColor(Color.alpha(0));
  		img.setText("Submit");
  		img.setTextColor(Color.BLACK);
  		img.setOnClickListener(this);
  		  		
     	params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
  		myText=new TextView(getApplicationContext());
  		myText.setId(400);
  		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
  		params.setMargins(0,(int)(2*y1+0.5*y1),0,0 );
  		myText.setLayoutParams(params);
  		myText.setTextColor(Color.BLACK);
  		myText.setTextSize(27.0f);
    //	myText.setOnClickListener(this);
  		myRelativeLayout.addView(myText);
         
  		RelativeLayout.LayoutParams newParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
	  	result=new TextView(getApplicationContext());
		result.setId(600);
  		newParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
  		newParams.setMargins(0,(int)((2*y1+0.90*y1)),0,0 );
  		result.setLayoutParams(newParams);
  		result.setTextSize(20.0f);
    	result.setTextColor(Color.BLACK);
  		myRelativeLayout.addView(result); 
        Score sc=new Score();
        score=sc.myScore;
    	TextView parent;
    	suggestions = new TextView[5];
    	
    	int suggestionId=700;
     	for(int j=0;j<5;j++){
			params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
			params1.setMargins(0, (int)((3*y1)+j*18+30),0,0);
			params1.addRule(RelativeLayout.CENTER_HORIZONTAL);
	    	suggestions[j]= new TextView(getApplicationContext());
			suggestions[j].setId(suggestionId++);
			suggestions[j].setLayoutParams(params1);
			suggestions[j].setTextSize(23.0f);
			suggestions[j].setTextColor(Color.BLACK);
			parent=suggestions[j];
			myRelativeLayout.addView(suggestions[j]);
		}
    	TextView scoreText=(TextView) findViewById(R.id.textView1);
        scoreText.setText("Score: " +Integer.toString(score));
        scoreText.setTextColor(Color.BLACK);
    	result.setText("");
    	myRelativeLayout.setBackgroundColor(Color.WHITE);
    	  mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE); // (1)
  		mySensorManager.registerListener(mySensorEventListener, mySensorManager
  				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
  				SensorManager.SENSOR_DELAY_NORMAL); // (2)
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
			    		  pressedButton="";
			    		  for (int i1 = 0; i1 < suggestions.length; i1++){
				    		  suggestions[i1].setText("");
			    		  }
			    		}
				public void onAccuracyChanged(Sensor sensor, int accuracy) {
				    /* can be ignored in this example */
				       }
			    };

		//End of Gesture Functions

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
		public void Reset(){
			top=0;
			for(int i=0;i<word.length();i++){
				
				buttonOriginal[i].setTextColor(Color.BLACK);
				buttonOriginal[i].setClickable(true);
				buttonPressed[i]=0;
			}
			//TextView tv=(TextView) findViewById(R.id.textView1);
			TextView tv1=(TextView) findViewById(myText.getId());
			tv1.setText("");
			result.setText("");
			//tv.setText("");
		//	firstW=true;

			
		}
		
		@Override
		public void onBackPressed() {
			   android.os.Process.killProcess(android.os.Process.myPid()) ;
			finish();
		}
		
		public static String removeChar(String s, char c) {

			   String r = "";
			   int flag=0;
			   for (int i = 0; i < s.length(); i ++) {
			      if ((s.charAt(i) != c) || (flag!=0)){ 
			    	  r += s.charAt(i);
			    	  
			      }
			      else
			    	  flag++;
			   }

			   return r;
			}
		
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// Removing the ability to touch the label
	/*	if(myText.getId()==v.getId()){
			
			String textString,my;
			TextView tapped=(TextView) findViewById(myText.getId());
			textString=tapped.getText().toString();
			my=textString;
			my=my.replaceAll(" ", "");
			if(my.equals("")){}
			else
			{
			textString=textString.substring(0, textString.length()-2);
			Log.d("arjun", textString);
			buttonOriginal[last[--top]].setClickable(true);
			buttonOriginal[last[top]].setTextColor(Color.BLACK);
			buttonPressed[last[top]]=0;
			tapped.setText(textString);
			}
			for (int i1 = 0; i1 < suggestions.length; i1++){
	    		  suggestions[i1].setText("");  
	    	  }
			int countViews=0;
			pressedButton=textString.replaceAll(" ", "").toLowerCase();
	    	if(top!=0){
		    	for(int j=0;j<enteredWords.size();j++){
		    		Log.d("top",pressedButton+" "+ enteredWords.get(j)+" "+Integer.toString(top));
		    		if(enteredWords.get(j).startsWith(pressedButton)){
		    			suggestions[enteredWords.get(j).length()-3].setText(suggestions[enteredWords.get(j).length()-3].getText().toString()+"  " + enteredWords.get(j).toUpperCase());
		    		}
		    	}
	    	}
		}*/
		
		if(v.getId()==img.getId()){
			
			int index;
			TextView tv=(TextView) findViewById(myText.getId());
			String entered=tv.getText().toString();
			entered=entered.replaceAll(" ", "");
			entered=entered.toLowerCase();
			Log.d("arjun", entered);
			index = Arrays.binarySearch(wordClass.wordRead, entered);
			pressedButton="";
  		  	for (int i1 = 0; i1 < suggestions.length; i1++){
  		  		suggestions[i1].setText("");
  		  	}
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
				result.setText("Word found!");
				TextView scoreText=(TextView) findViewById(R.id.textView1);
		        scoreText.setText("Score: " +Integer.toString(score));
				//TextView scoreText=(TextView) findViewById(R.id.score);
				//scoreText.setText("Score: " +Integer.toString(score));
				}}
			}
			
			Handler myHandler = new Handler();
			myHandler.postDelayed(mMyRunnable, 650);
			
			
			
		}
		else{
		String buttonText,text;
		int countViews=0;
		for (int i = 0; i < buttonOriginal.length; i++)
		   {
		      if (buttonOriginal[i].getId() == v.getId())
		      {
		    	  
		    	  if(buttonPressed[i]==1){
		    		  Log.d("arjun",buttonOriginal[i].getText().toString());
		    		  TextView tv=(TextView) findViewById(myText.getId());
		    		  buttonOriginal[i].setTextColor(Color.BLACK);
		    		  buttonPressed[i]=0;
		    		  char myString=buttonOriginal[i].getText().charAt(0);
		    		  String textString=tv.getText().toString();
		    		  textString=removeChar(textString, myString);
		    		  textString=textString.replaceAll("  "," ");
		    		  tv.setText(textString);
		    		//  top--;
		    		  for (int i1 = 0; i1 < suggestions.length; i1++){
			    		  suggestions[i1].setText("");  
			    	  }
					
					pressedButton=textString.replaceAll(" ", "").toLowerCase();
			    	if(top!=0){
				    	for(int j=0;j<enteredWords.size();j++){
				    		Log.d("top",pressedButton+" "+ enteredWords.get(j)+" "+Integer.toString(top));
				    		if(enteredWords.get(j).startsWith(pressedButton)){
				    			suggestions[enteredWords.get(j).length()-3].setText(suggestions[enteredWords.get(j).length()-3].getText().toString()+"  " + enteredWords.get(j).toUpperCase());
				    		}
				    	}

		    		  break;
		    		  
		    	  }}
		    	  else{
		    	  for (int i1 = 0; i1 < suggestions.length; i1++){
		    		  suggestions[i1].setText("");  
		    	  }
		    			
		    	 pressedButton=pressedButton+buttonOriginal[i].getText().toString().toLowerCase();
		    	 for(int j=0;j<enteredWords.size();j++){
		    		 
			         if(enteredWords.get(j).startsWith(pressedButton)){
			        	 suggestions[enteredWords.get(j).length()-3].setText(suggestions[enteredWords.get(j).length()-3].getText().toString()+"  " + enteredWords.get(j).toUpperCase());
		    		 }
		    	 }
		    	 Log.d("arjun","Button");
		    	 buttonPressed[i]=1;	 
		    	 buttonOriginal[i].setTextColor(Color.rgb(233, 150, 122));
		    	 //buttonOriginal[i].setClickable(false);
		   // 	 last[top++]=i;
		   // 	 Log.d("top", Integer.toString(top)+" " + Integer.toString(last[top-1])+" " + buttonOriginal[last[top-1]].getText().toString());
		         buttonText=buttonOriginal[i].getText().toString();
		         text=myText.getText().toString();
		    	 myText.setText(text+" "+buttonText);
		        
		    	 break;
		      }
		
		
		      }
		   }
		}
	
	}
	class temp implements Runnable {
	    
		public void run() {
	        // compute primes larger than minPrime
	    	    Log.d("arjun", "Before Hash");
	            GenerateHash gh=new GenerateHash(word);
	    		wordClass.wordRead=gh.createHash(myContext);
	    		wordClass.word=word;
	    		Log.d("arjun", Integer.toString(wordClass.wordRead.length));

	    	}
	}
	
	
}





