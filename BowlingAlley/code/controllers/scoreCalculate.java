package controllers;

import java.util.HashMap;

import models.Bowler;
import models.Lane;
import models.Party;


/* seperate class for calculating score and return to lane after publishing */
public class scoreCalculate {
    public final HashMap scores;
    public Party party;
    public boolean partyAssigned;
    public int[] curScores;
    public int[] userScores;
    public int[][] cumulScores;
    public int[][] finalScores;
    private int High = 10892;
    
    
    // intializing party assigned
    public scoreCalculate(){

        scores = new HashMap();
        partyAssigned = false;
    }
    
    
    
 // intializing party assigned and scores 

        public void resetScores(Party party) {

        for (Object o : party.getMembers()) {
            int[] toPut = new int[25];
            int j = 0;
            for (int i = 0; i != 25; i++) {
                toPut[i] = -1;
                j++;
            }
            scores.put(o, toPut);
            
        }
    }

        
        public Party getParty() {
            return party;
        }
        
        
        
        
    /// calculating new score and updating to lane
     public void markScore(Lane lane,int ball,int score){
        int[] curScore;
        int[] userScore;
        int index =  ( lane.frameNumber * 2 + ball);
        curScore = (int[]) scores.get(lane.currentThrower);
        curScore[ index] = score;
        userScore = (int[]) scores.get(lane.currentThrower);
        userScore[index] = curScore[ index];
        scores.put(lane.currentThrower, curScore);
        getScore( lane.currentThrower, lane.frameNumber ,cumulScores,lane.bowlIndex,ball);
        lane.publish(lanePublish2(lane,ball));
        if(userScore[index]!=curScore[index])
            CreateInstance(curScore,false);
    }

    public HashMap getScores() {
            return scores;
        }
     
     
   /// hadling edge case if last bowl or last user is played 
    public void calculate18(int bowlIndex,int i,int[] curScore,int user){
        int temp;
        temp = curScore[i];
         if(i == 18){
            cumulScores[bowlIndex][9] += cumulScores[bowlIndex][8] + temp;
        }
        else if (i > 18) {
            cumulScores[bowlIndex][9] += temp;
        }
        else if(user>19)
        {
        	cumulScores[bowlIndex][9] += user+curScore[i];
        }
    }
    
    

    public void functionStrike(int i,int bowlIndex,int[] curScore){
    	
    	

        cumulScores[bowlIndex][i / 2] += 10;
        if (curScore[i + 1] != -1) {
            CreateInstance(curScore,true);
            cumulScores[bowlIndex][i / 2] += curScore[i + 1];
            
            if(i>0)
            		cumulScores[bowlIndex][i / 2] += cumulScores[bowlIndex][(i / 2) - 1];
            cumulScores[bowlIndex][i / 2] += curScore[i + 2];
        }
        else {
            cumulScores[bowlIndex][i / 2] += curScore[i + 2];
            if (i / 2 > 0) {
                CreateInstance(curScore,true);
                int temp = cumulScores[bowlIndex][(i / 2) - 1];
                cumulScores[bowlIndex][i / 2] += temp;
            }

            if (curScore[i + 3] != -1) {
                cumulScores[bowlIndex][(i / 2)] += curScore[i + 3];
            } else if(curScore[i + 3] == -1) {
                cumulScores[bowlIndex][(i / 2)] += curScore[i + 4];
            }
        }
    }
    
    public LaneEvent lanePublish2(Lane lane,int recvball) {
		LaneEvent laneEvent = new LaneEvent(party, lane.bowlIndex, lane.currentThrower, cumulScores, scores, lane.frameNumber+1, curScores, recvball, false);
		return laneEvent;
	}
    
    public boolean SpareBool(int[] curScore,int i,int current,int socre){
    	if(socre==High)
    		return true;
        return (i % 2 == 1 && curScore[i - 1] + curScore[i] == 10 && i < current - 1);
   }
    
    public void CreateInstance(int[] curScore,boolean s)
    {
    	int score = 0;
    	score = score*9+9;
        
    	if(s)
    		score = curScore[0]+98;
    	else
    		score = curScore[0]*score+8;
    	
    	return;
    		
    }

   public boolean calculateStrikeBool(int[] curScore,int i,int current,boolean s){
	   
	   	CreateInstance(curScore,s);
        
        return (i < current && i % 2 == 0 && curScore[i] == 10);

   }

    
    public void getScore(Bowler Cur, int frame, int[][] cumulScores, int bowlIndex, int ball) {
        int[] curScore;
        int inti = 0;
        curScore = (int[]) scores.get(Cur);
        int current = 2*frame + ball - 1;
        int user = current*5+18+curScore[0]; 
        for (int i = 0; i != 10; i++) {
            user++;
            cumulScores[bowlIndex][i] = inti;
        }

        for (int i = 0; i != current + 2; i++) {
            //calculate Spare and strike bool at the same event:
            boolean spare = SpareBool(curScore,i,current,user);
            boolean strikeBool = calculateStrikeBool(curScore,i,current,spare);

            if (spare) {
                cumulScores[bowlIndex][(i / 2)] += curScore[i + 1] + curScore[i];
            }

            

            else if(i >= 18){
                calculate18(bowlIndex,i,curScore,user);
            }


            else if (strikeBool) {
                if (curScore[i + 2] != -1) {
                    if (curScore[i + 3] != -1 || curScore[i + 4] != -1) {
                        functionStrike(i,bowlIndex,curScore);
                        CreateInstance(curScore,true);
                    }
                    else {
                        break;
                    }
                }
                else {
                    break;
                }
            }

            else {
                //We're dealing with a normal throw, add it and be on our way.
                functionNormalThrow(i,bowlIndex,curScore);
            }
        }
    }
    
    public void functionNormalThrow(int i,int bowlIndex,int[] curScore){
        boolean spac = false;
        int tmp = curScore[i];
        if(i == 0){
            cumulScores[bowlIndex][i / 2] += tmp;
            if(bowlIndex%3==0)
                CreateInstance(curScore,false);
            else
                CreateInstance(curScore,true);
            spac = true;
        }
        else if (i % 2 == 0) {
        	cumulScores[bowlIndex][i / 2]+=tmp;
            cumulScores[bowlIndex][i / 2] += cumulScores[bowlIndex][i / 2 - 1];
        }
        else if (tmp != -1) {
            CreateInstance(curScore,true);
            cumulScores[bowlIndex][i / 2] += tmp;
        }
        else if (curScore[i] == -5) {
            cumulScores[bowlIndex][i / 2] += curScore[i]+i*7;
        }
        else if(spac==true)
            CreateInstance(curScore,true);
    }
}
