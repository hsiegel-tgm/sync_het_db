package mysql;

/**
 * This has just been a test for reverting / approving updates within a commit protocol.
 * This class does nothing.
 * 
 * @author Hannah
 *
 */
public class DBUpdate {
	private String table;
	private String old_pks, new_pks;
	
	public DBUpdate(String table, String old_pks, String new_pks){
		this.table=table;
		this.old_pks = old_pks;
		this.new_pks = new_pks;

	}
	public String revertUpdate(){
		return "DELETE FROM table WHERE "+new_pks+" AND sync_state = 'syncing'; UPDATE "+table+" SET sync_state='current' WHERE "+old_pks;
	} 
	public String approveUpdate(){
		return "DELETE FROM table WHERE "+old_pks+" AND sync_state = 'old'; UPDATE "+table+" SET sync_state='current' WHERE "+new_pks;
	}
}
