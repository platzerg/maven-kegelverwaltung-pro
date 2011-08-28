/**
 * 
 */
package platzerworld.kegeln.klasse.db;

/**
 * Schnittstelle zur Tabelle KLASSE. 
 * Die Klasse liefert SQL-Code zur Erzeugung der Tabelle SQL-Code f�r alle f�r Kegelverwaltung erforderlichen Statements
 * 
 * 
 * @author platzerg
 */
public final class KlasseTbl implements KlasseColumns {
	/**
	 * Name der Datenbanktabelle.
	 */
	public static final String TABLE_NAME = "klasse";

	/**
	 * SQL Anweisung zur Schemadefinition.
	 */
	public static final String SQL_CREATE = "CREATE TABLE KLASSE (_id   INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL)";

	/**
	 * Standard-Sortierreihenfolge f�r die Tabelle.
	 * 
	 * Sortiert wird nach NAME absteigend.
	 */
	public static final String DEFAULT_SORT_ORDER = NAME + " DESC";

	/**
	 * SQL Anweisung zur L�schung der Tabelle.
	 */
	public static final String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

	/**
	 * SQL Anweisung f�r Erzeugung einer Minimal-KLASSE aus Name.
	 */
	public static final String STMT_MIN_INSERT = "INSERT INTO KLASSE " + "(name) " + "VALUES (?)";

	/**
	 * SQL Anweisung f�r Erzeugung einer Klasse aus den Stammdaten Name.
	 */
	public static final String STMT_KLASSE_INSERT = "INSERT INTO KLASSE " + "(name) " + "VALUES (?)";

	/**
	 * SQL-Anweisung zur L�schung aller Klassen.
	 */
	public static final String STMT_KLASSE_DELETE = "DELETE from KLASSE ";

	/**
	 * SQL-Anweisung zur L�schung einer Klasse anhand seines Schl�ssewerts.
	 */
	public static final String STMT_KLASSE_DELETE_BY_ID = "DELETE KLASSE " + "WHERE _id = ?";

	/** Liste aller bekannten Attribute. */
	public static final String[] ALL_COLUMNS = new String[] { ID, NAME };

	/**
	 * WHERE-Bedingung f�r ID-Anfrage.
	 */
	public static final String WHERE_ID_EQUALS = ID + "=?";

	/**
	 * Klasse enth�lt nur Konstanten. Daher keine Objekterzeugung vorgesehen.
	 */
	private KlasseTbl() {
	}
}
