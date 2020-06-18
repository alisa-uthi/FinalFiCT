//Alisa Uthikamporn 6188025 Section 1
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

//import Player.PlayerType;

public class Arena {

	public enum Row {Front, Back};	//enum for specifying the front or back row
	public enum Team {A, B};		//enum for specifying team A or B
	
	private Player[][] teamA = null;	//two dimensional array representing the players of Team A
	private Player[][] teamB = null;	//two dimensional array representing the players of Team B
	private int numRowPlayers = 0;		//number of players in each row					
	
	public static final int MAXROUNDS = 100;	//Max number of turn
	public static final int MAXEACHTYPE = 3;	//Max number of players of each type, in each team.
	private final Path logFile = Paths.get("battle_log.txt");
	
	private int numRounds = 0;      			//keep track of the number of rounds so far
	private int count[][] = new int[2][6];  	//Count for checking in Team A or B in each type
	private int sumPlayer[] = new int[2]; 		//Sum of Players in each team 
	private static double sumHP;
	private int numAliveA = 0, numAliveB = 0;
	
	/**
	 * Constructor. 
	 * @param _numRowPlayers is the number of players in each row.
	 */
	public Arena(int _numRowPlayers)
	{	
		numRowPlayers = _numRowPlayers;	
		this.teamA = new Player[2][numRowPlayers];
		this.teamB = new Player[2][numRowPlayers];
		
		try {
			Files.deleteIfExists(logFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns true if "player" is a member of "team", false otherwise.
	 * Assumption: team can be either Team.A or Team.B
	 * @param player
	 * @param team
	 * @return
	 */
	public boolean isMemberOf(Player player, Team team)
	{
		switch(team)
		{
			//if this player is member of team A
			case A:															
			{
				for(int i = 0; i < 2; i++)
				{
					for(int j = 0; j < teamA[i].length; j++)
					{
					if(player.equals(teamA[i][j]))				
						return true;
					}
				}
				break;
			}
			
			//if this player is member of team B
			case B:
			{
				for(int i = 0; i < 2; i++)
				{
					for(int j = 0; j < teamB[i].length; j++)
					{
					if(player.equals(teamB[i][j]))				
						return true;
					}
				}
				break;
			}
		}		
		return false;
	}
	
	/**
	 * This methods receives a player configuration (i.e., team, type, row, and position), 
	 * creates a new player instance, and places him at the specified position.
	 * @param team is either Team.A or Team.B
	 * @param pType is one of the Player.Type  {Healer, Tank, Samurai, BlackMage, Phoenix}
	 * @param row	either Row.Front or Row.Back
	 * @param position is the position of the player in the row. Note that position starts from 1, 2, 3....
	 */
	public void addPlayer(Team team, Player.PlayerType pType, Row row, int position)
	{	
		Player playerTy = new Player(pType);
		switch(team)
		{
			case A:
			{
				switch (row)
				{
					case Front:
					{
						teamA[0][position-1] = playerTy;
						teamA[0][position-1].setInfo(team, row, position);		//set team,row,position of this player type
						break;
					}
					case Back:
					{
						teamA[1][position-1] = playerTy;
						teamA[1][position-1].setInfo(team, row, position);		//set team,row,position of this player type
						break;
					}
				}
				break;
			}
			case B:
			{
				switch (row)
				{
					case Front:
					{
						teamB[0][position-1] = playerTy;
						teamB[0][position-1].setInfo(team, row, position);		//set team,row,position of this player type
						break;
					}
					case Back:
					{
						teamB[1][position-1] = playerTy;
						teamB[1][position-1].setInfo(team, row, position);		//set team,row,position of this player type
						break;
					}
				}
				break;
			}
		}
	}
	
	/**
	 * Validate the players in both Team A and B. Returns true if all of the following conditions hold:
	 * 
	 * 1. All the positions are filled. That is, there each team must have exactly numRow*numRowPlayers players.
	 * 2. There can be at most MAXEACHTYPE players of each type in each team. For example, if MAXEACHTYPE = 3
	 * then each team can have at most 3 Healers, 3 Tanks, 3 Samurais, 3 BlackMages, 3 Phoenixes, and 3 Cherry.
	 * 
	 * Returns true if all the conditions above are satisfied, false otherwise.
	 * @return
	 */
	public boolean validatePlayers()
	{
		for(int i = 0; i < 2; i++)
		{
			for(int j = 0; j < numRowPlayers; j++)
			{
				//count[0][] for team A. Check how many player's type in this team
				if(teamA[i][j].getType().equals(Player.PlayerType.Healer))			count[0][0]++;
				else if(teamA[i][j].getType().equals(Player.PlayerType.Tank))		count[0][1]++;
				else if(teamA[i][j].getType().equals(Player.PlayerType.Samurai))	count[0][2]++;
				else if(teamA[i][j].getType().equals(Player.PlayerType.BlackMage))	count[0][3]++;
				else if(teamA[i][j].getType().equals(Player.PlayerType.Phoenix))	count[0][4]++;
				else if(teamA[i][j].getType().equals(Player.PlayerType.Cherry))		count[0][5]++;
				
				//count[1][] for team B. Check how many player's type in this team
				if(teamB[i][j].getType().equals(Player.PlayerType.Healer))			count[1][0]++;
				else if(teamB[i][j].getType().equals(Player.PlayerType.Tank))		count[1][1]++;
				else if(teamB[i][j].getType().equals(Player.PlayerType.Samurai))	count[1][2]++;
				else if(teamB[i][j].getType().equals(Player.PlayerType.BlackMage))	count[1][3]++;
				else if(teamB[i][j].getType().equals(Player.PlayerType.Phoenix))	count[1][4]++;
				else if(teamB[i][j].getType().equals(Player.PlayerType.Cherry))		count[1][5]++;
			}
		}
		for(int m = 0; m < 2; m++)
		{
			for(int k = 0; k < 6; k++)
			{
				if(count[m][k] > MAXEACHTYPE)
					return false;
				else 
					sumPlayer[m] += count[m][k];		//count the sum of the player in each team
			}
		}
		if(sumPlayer[0] == 2*numRowPlayers && sumPlayer[1] == 2*numRowPlayers)		//check whether sum of player in each team exceeds or not
			return true;
		else
			return false;
	}
	
	/**
	 * Returns the sum of HP of all the players in the given "team"
	 * @param team
	 * @return
	 */
	public static double getSumHP(Player[][] team)
	{
		sumHP = 0; 
		for(int i = 0; i < 2; i++) 				
		{
			for(int j = 0; j < team[i].length; j++)
				sumHP += team[i][j].getCurrentHP();
		}	
		return sumHP;
	}
	
	/**
	 * Return the team (either teamA or teamB) whose number of alive players is higher than the other. 
	 * 
	 * If the two teams have an equal number of alive players, then the team whose sum of HP of all the
	 * players is higher is returned.
	 * 
	 * If the sums of HP of all the players of both teams are equal, return teamA.
	 * @return
	 */
	public Player[][] getWinningTeam()
	{
		for(int i = 0; i < 2; i++)
		{
			for(int j = 0; j < numRowPlayers; j++)
			{
				if(teamA[i][j].getCurrentHP() != 0)
					numAliveA++;
				if(teamB[i][j].getCurrentHP() != 0)
					numAliveB++;					
			}
		}
		
		if(numAliveA > numAliveB)
			return teamA;
		else if(numAliveA == numAliveB) 
		{
			if(getSumHP(teamA) >= getSumHP(teamB))
				return teamA;
			else
				return teamB;
		}
		else
			return null;
	}

	/**
	 * This method simulates the battle between teamA and teamB. The method should have a loop that signifies
	 * a round of the battle. In each round, each player in teamA invokes the method takeAction(). The players'
	 * turns are ordered by its position in the team. Once all the players in teamA have invoked takeAction(),
	 * not it is teamB's turn to do the same. 
	 * 
	 * The battle terminates if one of the following two conditions is met:
	 * 
	 * 1. All the players in a team has been eliminated.
	 * 2. The number of rounds exceeds MAXROUNDS
	 * 
	 * After the battle terminates, report the winning team, which is determined by getWinningTeam().
	 */
	public void startBattle()
	{
		while(true)
		{
			if(numRounds < MAXROUNDS && sumHP != 0)
			{
				System.out.println("Round : " + (numRounds+1));
				for(int k = 0; k < 2; k++)
				{
					for(int j = 0; j < numRowPlayers; j++)
					{
						if(teamA[k][j].isAlive())
							teamA[k][j].takeAction(this);	
					}
				}
				
				for(int m = 0; m < 2; m++)
				{
					for(int p = 0; p < numRowPlayers; p++)
					{
						if(teamB[m][p].isAlive())
							teamB[m][p].takeAction(this);															 		
					}
				}
				
				displayArea(this, true);
				logAfterEachRound();
				numRounds++;
			}
			
			else 	break;
		}
			
		//after the end of the battle, get the winning team
		if(isMemberOf(getWinningTeam()[0][0], Arena.Team.A))
			System.out.println("Team A Win!!!!");
		else if (isMemberOf(getWinningTeam()[0][0], Arena.Team.B))
			System.out.println("Team B Win!!!!");
	}
	
	public Player[][] getTargetTeam(Player player)
	{
		if(isMemberOf(player,Team.A))
			return teamB;
		else if (isMemberOf(player,Team.B))	
			return teamA;
		return null;
	}
	
	public Player[][] getMyTeam(Player player)
	{
		if(isMemberOf(player,Team.A))
			return teamA;
		else if (isMemberOf(player,Team.B))	
			return teamB;
		return null;
	}

	/**
	 * This method displays the current area state, and is already implemented for you.
	 * In startBattle(), you should call this method once before the battle starts, and 
	 * after each round ends. 
	 * 
	 * @param arena
	 * @param verbose
	 */
	public static void displayArea(Arena arena, boolean verbose)
	{
		StringBuilder str = new StringBuilder();
		if(verbose)
		{
			str.append(String.format("%43s   %40s","Team A","")+"\t\t"+String.format("%-38s%-40s","","Team B")+"\n");
			str.append(String.format("%43s","BACK ROW")+String.format("%43s","FRONT ROW")+"  |  "+String.format("%-43s","FRONT ROW")+"\t"+String.format("%-43s","BACK ROW")+"\n");
			for(int i = 0; i < arena.numRowPlayers; i++)
			{
				str.append(String.format("%43s",arena.teamA[1][i])+String.format("%43s",arena.teamA[0][i])+"  |  "+String.format("%-43s",arena.teamB[0][i])+String.format("%-43s",arena.teamB[1][i])+"\n");
			}
		}
	
		str.append("@ Total HP of Team A = "+getSumHP(arena.teamA)+". @ Total HP of Team B = "+getSumHP(arena.teamB)+"\n\n");
		System.out.print(str.toString());	
	}
	
	/**
	 * This method writes a log (as round number, sum of HP of teamA, and sum of HP of teamB) into the log file.
	 * You are not to modify this method, however, this method must be call by startBattle() after each round.
	 * 
	 * The output file will be tested against the auto-grader, so make sure the output look something like:
	 * 
	 * 1	47415.0	49923.0
	 * 2	44977.0	46990.0
	 * 3	42092.0	43525.0
	 * 4	44408.0	43210.0
	 * 
	 * Where the numbers of the first, second, and third columns specify round numbers, sum of HP of teamA, and sum of HP of teamB respectively. 
	 */
	private void logAfterEachRound()
	{
		try {
			Files.write(logFile, Arrays.asList(new String[]{numRounds+"\t"+getSumHP(teamA)+"\t"+getSumHP(teamB)}), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}	
}
 