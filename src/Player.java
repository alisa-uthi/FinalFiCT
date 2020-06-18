//Alisa Uthikamporn 6188025 Section 1
//import Arena.Team;
import sun.security.action.GetLongAction;

public class Player {

	public class StudentTester {

	}

	public enum PlayerType {Healer, Tank, Samurai, BlackMage, Phoenix, Cherry};
	
	private PlayerType type; 	//Type of this player. Can be one of either Healer, Tank, Samurai, BlackMage, or Phoenix
	private double maxHP;		//Max HP of this player
	private double currentHP;	//Current HP of this player 
	private double atk;			//Attack power of this player
	private int numSpecialTurns,internalTurn;
	private boolean Taunting = false, Cursed = false, Sleeping = false;
	private static int positionOfLowestHP1,positionOfLowestHP2,allDeadFront;
	private static double lowestHP;
	private static Player target = null,curserOfA,curserOfB,sleepOfA,sleepOfB;
	private Player[][] Team;
	private Arena.Team teamm;
	private Arena.Row roww;
	private int positionn;
	
	/**
	 * Constructor of class Player, which initializes this player's type, maxHP, atk, numSpecialTurns, 
	 * as specified in the given table. It also reset the internal turn count of this player. 
	 * @param _type
	 */
	public Player(PlayerType _type)
	{	
		this.type = _type;
		switch(_type)
		{
			case Healer:
			{
				this.maxHP = 4790;			this.numSpecialTurns = 4;
				this.currentHP = 4790;		this.internalTurn = 0;
				this.atk = 238;				break;
			}
			case Tank:
			{
				this.maxHP = 5340;			this.numSpecialTurns = 4;
				this.currentHP = 5340;		this.internalTurn = 0;
				this.atk = 255;				break;
			}
			case Samurai:
			{
				this.maxHP = 4005;			this.numSpecialTurns = 3;
				this.currentHP = 4005;		this.internalTurn = 0;
				this.atk = 368;				break;
			}
			case BlackMage:
			{
				this.maxHP = 4175;			this.numSpecialTurns = 4;
				this.currentHP = 4175;		this.internalTurn = 0;
				this.atk = 303;				break;
			}
			case Phoenix:
			{
				this.maxHP = 4175;			this.numSpecialTurns = 8;
				this.currentHP = 4175;		this.internalTurn = 0;
				this.atk = 209;				break;
			}
			case Cherry:
			{
				this.maxHP = 3560;			this.numSpecialTurns = 4;
				this.currentHP = 3560;		this.internalTurn = 0;
				this.atk = 198;				break;
			}
		}
	}
	
	/**
	 * Returns the current HP of this player
	 * @return
	 */
	public double getCurrentHP()
	{
		return currentHP;
	}
	
	/**
	 * Returns type of this player
	 * @return
	 */
	public Player.PlayerType getType()
	{
		return type;
	}
	
	/**
	 * Returns max HP of this player. 
	 * @return
	 */
	public double getMaxHP()
	{		
		return maxHP;
	}
	
	/**
	 * Returns whether this player is sleeping.
	 * @return
	 */
	public boolean isSleeping()
	{
		if(Sleeping)	return true;
		else			return false;
	}
	
	/**
	 * Returns whether this player is being cursed.
	 * @return
	 */
	public boolean isCursed()
	{
		if(Cursed)	return true;
		else		return false;
	}
	
	/**
	 * Returns whether this player is taunting.
	 * @return
	 */
	public boolean isTaunting()
	{
		if(Taunting)	return true;
		else			return false;
	}
	
	/**
	 * Returns whether this player is alive (i.e. current HP > 0).
	 * @return
	 */
	public boolean isAlive()
	{
		if(this.getCurrentHP() > 0)		return true;
		else 							return false;
	}
	
	public void resetStatus()   
	{
		target.Taunting = false;
		target.Cursed = false; 
		target.Sleeping = false;
	}
	
	public static Player findDeadAlly(Player[][] myTeam)
	{
		for(int m = 0; m < 2; m++)
		{
			for(int j = 0; j < myTeam[m].length ; j++)
			{
				if(myTeam[m][j].currentHP == 0)
					return myTeam[m][j];
			}		
		}
		return null;
	}
	
	public static Player getPlayerLowestHP(Player[][] team)          
	{
		lowestHP = 999999999;
		allDeadFront = 0;
		
		//check if all of the players in the front row are dead
		for(int i = 0; i < team[0].length; i++)
		{
			if(team[0][i].currentHP == 0)
				allDeadFront++;
		}
		
		//if some of the players in the front row still alive
		if(allDeadFront != team[0].length)
		{
			for(int j = team[0].length - 1; j >= 0 ; j--)							//for team A
			{
				if(team[0][j].currentHP <= lowestHP && team[0][j].isAlive())
				{
					lowestHP = team[0][j].currentHP;
					positionOfLowestHP1 = 0;
					positionOfLowestHP2 = j;					
				}
			}
		}
		
		else
		{
			for(int j = team[1].length - 1; j >= 0 ; j--)							//for team B
			{
				if(team[1][j].currentHP <= lowestHP && team[1][j].isAlive())
				{
					lowestHP = team[1][j].currentHP;
					positionOfLowestHP1 = 1;
					positionOfLowestHP2 = j;					
				}
			}
		}	
		return team[positionOfLowestHP1][positionOfLowestHP2];						//return the player who has the lowest HP
	}
	
	public Player findTarget(Player[][] theirTeam)					
	{
		for(int m = 0; m < 2; m++)
		{
			for(int j = 0; j < theirTeam[m].length; j++)
			{
				if(theirTeam[m][j].isTaunting() && theirTeam[m][j].isAlive())		//find the target who is taunting first
					{
						target = theirTeam[m][j];
						return target;
					}
				else 
					target = getPlayerLowestHP(theirTeam);							//find the target who has the lowest HP
			}
		} 
		return target;
	}
	
	public void attack(Player target)
	{			
		if(this.atk < target.currentHP)
			target.currentHP -= this.atk;
		else
		{
			target.currentHP = 0;
			target.resetStatus();
		}
	}
	
	public void useSpecialAbility(Player[][] myTeam, Player[][] theirTeam) 
	{								
		Player target = null;
		switch(type)
		{
			case Healer:	
			{
				target = getPlayerLowestHP(myTeam);
				System.out.print(this.playerInfo() + " heals " + target.playerInfo());
				if(target.isCursed() == false)
				{
					target.currentHP += 0.25 * target.getMaxHP();
					if(target.currentHP >= target.getMaxHP())	
						target.currentHP = target.getMaxHP();
				}					
				System.out.println(" --> HP of " + target.playerInfo() + "= " + target.currentHP);		//display target's HP
				break;
			}
			case Tank:			
			{
				System.out.println(this.playerInfo() + " uses Special ability of being taunting.");
				this.Taunting = true;		//change the status of this player to be taunting
				this.isTaunting();
				break;
			}
			case Samurai:		
			{
				target = getPlayerLowestHP(theirTeam);
				System.out.print(this.playerInfo() + " uses Doubled-Slash to " + target.playerInfo());
				attack(target);
				attack(target);
				System.out.println(" --> HP of " + target.playerInfo() + " = " + target.currentHP);		//display target's HP
				break;
			}
			case BlackMage:
			{
				target = getPlayerLowestHP(theirTeam);
				System.out.println(this.playerInfo() + " curses " + getPlayerLowestHP(theirTeam).playerInfo());
				for(int m = 0; m < 2; m++)
				{
					for(int j = myTeam[m].length - 1; j >= 0 ; j--)			
					{
						//assign this player to be a curser so that when it is this player's turn again, I can check the status of the target
						if(this.equals(myTeam[m][j]))
							curserOfA = this;				
						else
							curserOfB = this;
					}
				}	
				target.Cursed = true;			//change the status of the target to be cursed
				target.isCursed();
				break;
			}
			case Phoenix: 		
			{
				target = findDeadAlly(myTeam);
				if(findDeadAlly(myTeam) != null)
				{
					System.out.println(this.playerInfo() + " revives " + target.playerInfo() + " from the death.");
					target.currentHP += (0.3 * target.maxHP);
					target.internalTurn = 0;
					target.resetStatus();
				}	
				break;
			}
			case Cherry:
			{
				System.out.println(this.playerInfo() + " uses Fortune Cookie to the opposite team.");
				for(int m = 0; m < 2; m++)
				{
					for(int j = theirTeam[m].length - 1; j >= 0 ; j--)
					{
						if(theirTeam[m][j].currentHP != 0)
						{
							theirTeam[m][j].Sleeping = true;			//change the status of the opposite team to be sleeping
							theirTeam[m][j].isSleeping();
						}
					}
				}	
				for(int m = 0; m < 2; m++)
				{
					for(int j = myTeam[m].length - 1; j >= 0 ; j--)
					{
						//assign this player to be a sleeper so that when it is this player's turn again, I can check the status of the target
						if(this.equals(myTeam[m][j]))
							sleepOfA = this;
						else
							sleepOfB = this;
					}
				}
				break;
			}
		}
	}
		
	/**
	 * This method is called by Arena when it is this player's turn to take an action. 
	 * By default, the player simply just "attack(target)". However, once this player has 
	 * fought for "numSpecialTurns" rounds, this player must perform "useSpecialAbility(myTeam, theirTeam)"
	 * where each player type performs his own special move. 
	 * @param arena
	 */
	public void takeAction(Arena arena)			
	{	
		findTarget(arena.getTargetTeam(this));
		Team = arena.getTargetTeam(this);			//find the target's team
		this.internalTurn++;	
		
		if(this.isTaunting())
			this.Taunting = false;
		
		for(int m = 0; m < 2; m++)
		{
			for(int j = 0; j < this.Team[m].length ; j++)
			{
				if(this.equals(curserOfA) || this.equals(curserOfB))
					Team[m][j].Cursed = false;								//the target's status is reset if this is the turn of the curser
				if(this.equals(sleepOfA) || this.equals(sleepOfB))
					Team[m][j].Sleeping = false;							//the target's status is reset if this is the turn of the sleeper
			}			
		}
		
		if(this.isSleeping())
		{
			System.out.println(this.playerInfo() + " is sleeping. Pass this action.");
			this.internalTurn--;
		}	
		else if(this.internalTurn % this.numSpecialTurns == 0)				//if this turn is the player's internal turn, the player will use special ability
			this.useSpecialAbility(arena.getMyTeam(this),Team);
		else
		{
			this.attack(target);
			System.out.print(this.playerInfo() + " attacks " + target.playerInfo());
			System.out.println(" --> HP of " + target.playerInfo() + " = " + target.currentHP);
		}
	}
		
	public String playerInfo()
	{
		return "[" + this.teamm + "][" + this.roww + "][" + this.positionn + "] (" + getType() + ")"; 
	}
	
	public void setInfo(Arena.Team team, Arena.Row row, int position)
	{
		this.teamm = team;
		this.roww = row;
		this.positionn = position;
	}

	/**
	 * This method overrides the default Object's toString() and is already implemented for you. 
	 */
	@Override
	public String toString()
	{
		return "["+this.type.toString()+" HP:"+this.currentHP+"/"+this.maxHP+" ATK:"+this.atk+"]["
				+((this.isCursed())?"C":"")
				+((this.isTaunting())?"T":"")
				+((this.isSleeping())?"S":"")
				+"]";
	}	
}