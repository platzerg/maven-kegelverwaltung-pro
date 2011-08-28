/**
 * 
 */
package platzerworld.kegeln.mannschaft.db;

/**
 * Schnittstelle zur Tabelle MANNSCHAFT. <br>
 * Die Klasse liefert SQL-Code zur Erzeugung der Tabelle SQL-Code f�r alle f�r Kegelverwaltung erforderlichen Statements
 * 
 * @author platzerg
 */
public final class MannschaftTbl implements MannschaftColumns {
	/**
	 * Name der Datenbanktabelle.
	 */
	public static final String TABLE_NAME = "mannschaft";

	/**
	 * SQL Anweisung zur Schemadefinition.
	 */
	public static final String SQL_CREATE = "CREATE TABLE MANNSCHAFT (_id   INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, mannschaft INTEGER, klasse_id INTEGER, verein_id INTEGER, FOREIGN KEY(verein_id) REFERENCES VEREIN(_id), FOREIGN KEY(klasse_id) REFERENCES KLASSE(_id));";

	/**
	 * Standard-Sortierreihenfolge f�r die Tabelle. Sortiert wird nach Name absteigend.
	 */
	public static final String DEFAULT_SORT_ORDER = NAME + " DESC";

	/**
	 * SQL Anweisung zum L�schen der Tabelle.
	 */
	public static final String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

	/**
	 * SQL Anweisung f�r Erzeugung einer Minimal-Mannschaft aus Name und KlasseId.
	 */
	public static final String STMT_MIN_INSERT = "INSERT INTO MANNSCHAFT " + "(name, klasse_id, verein_id) " + "VALUES (?,?,?)";

	/**
	 * SQL Anweisung f�r Erzeugung einer Mannschaft aus den Stammdaten Name, KlasseId, Mannschaft.
	 */
	public static final String STMT_NAME_KLASSE_MANNSCHAFT_INSERT = "INSERT INTO mannschaft "
			+ "(name, klasse_id, verein_id, mannschaft) " + "VALUES (?,?,?,?)";

	/**
	 * SQL-Anweisung zum L�schen aller Mannschaften.
	 */
	public static final String STMT_MANNSCHAFT_DELETE = "DELETE from mannschaft ";

	/**
	 * SQL-Anweisung zum L�schen einer Mannschaft anhand seines Schl�sselwerts.
	 */
	public static final String STMT_MANNSCHAFT_DELETE_BY_ID = "DELETE mannschaft " + "WHERE _id = ?";

	/**
	 * SQL-Anweisung zum L�schen eines Geokontakts anhand seines Namens und KlasseId
	 */
	public static final String STMT_MANNSCHAFT_DELETE_BY_ID_KLASSE_ID = "DELETE mannschaft WHERE name = ? and klasse_id = ? and verein_id = ?";

	/** Liste aller bekannten Attribute. */
	public static final String[] ALL_COLUMNS = new String[] { ID, NAME, KLASSE_ID, VEREIN_ID, MANNSCHAFT };

	/**
	 * WHERE-Bedingung �r ID-Anfrage.
	 */
	public static final String WHERE_ID_EQUALS = ID + "=?";

	/**
	 * WHERE-Bedingung f�r ID-Anfrage und KlasseId.
	 */
	public static final String WHERE_ID_KLASSE_ID_EQUALS = ID + "=? and " + KLASSE_ID + "=? and " +VEREIN_ID + "=?";

	/**
	 * Klasse enth�lt nur Konstanten. Daher keine Objekterzeugung vorgesehen.
	 */
	private MannschaftTbl() {
	}
}
