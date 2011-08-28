/**
 * 
 */
package platzerworld.kegeln;

import platzerworld.kegeln.ergebnis.db.ErgebnisTbl;
import platzerworld.kegeln.klasse.KlassenGenerator;
import platzerworld.kegeln.klasse.db.KlasseTbl;
import platzerworld.kegeln.mannschaft.db.MannschaftTbl;
import platzerworld.kegeln.spieler.db.SpielerTbl;
import platzerworld.kegeln.verein.db.VereinTbl;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

/**
 * Die Klasse dient als logische Verbindung zur Datenbank der Anwendung. <br>
 * Sie ist für die Erstellung und Wartung des Datenbankschemas sowie die
 * Initialbefüllung der Tabellen verantwortlich.
 * 
 * @author pant
 */
public class KegelverwaltungDatenbank extends SQLiteOpenHelper {

	/** Markierung für Logging. */
	private static final String TAG = "KegelverwaltungDatenbank";

	/** Name der Datenbankdatei. */
	private static final String DATENBANK_NAME = "kegelverwaltung.db";

	private static SQLiteDatabase db;
	private final Context mContext;
	/**
	 * Version des Schemas.
	 * <ul>
	 * <li>1 : Initiales Schema
	 * <li>2 : VORNAME raus. LOOKUP_KEY neu
	 * <li>3 : neue Tabelle FOTOS
	 * <li>4 : neue Spalte FOTOS.stichwort_pos
	 * </ul>
	 */
	private static final int DATENBANK_VERSION = 1;

	/**
	 * Die Datenbank kann nur nach Kenntnis "ihrer" Anwendung verwaltet werden.
	 * Daher muss der Context der Anwendung übergeben werden.
	 * 
	 * @param context
	 *            Context der aufrufenden Anwendung.
	 */
	public KegelverwaltungDatenbank(Context context) {
		super(context, DATENBANK_NAME, null, DATENBANK_VERSION);
		mContext = context;
	}
	
	public void resetDB(){
		SQLiteDatabase dbCon = null;
		try{
		 dbCon = getWritableDatabase();
			if(null != dbCon){
				dbCon.execSQL(ErgebnisTbl.SQL_DROP);
				dbCon.execSQL(SpielerTbl.SQL_DROP);
				dbCon.execSQL(MannschaftTbl.SQL_DROP);
				dbCon.execSQL(VereinTbl.SQL_DROP);
				dbCon.execSQL(KlasseTbl.SQL_DROP);
				
				dbCon.close();
				
				this.db = getWritableDatabase();
				initDatabase(mContext);
			}
		} finally {
			dbCon.close();
		}
	}
	
	public void deleteTable(){
		SQLiteDatabase dbCon = null;
		try{
		 dbCon = getWritableDatabase();
			if(null != dbCon){
				dbCon.execSQL(ErgebnisTbl.SQL_DROP);
				dbCon.execSQL(SpielerTbl.SQL_DROP);
				dbCon.execSQL(MannschaftTbl.SQL_DROP);
				dbCon.execSQL(VereinTbl.SQL_DROP);
				dbCon.execSQL(KlasseTbl.SQL_DROP);
				
				dbCon.close();
			}
		} finally {
			dbCon.close();
		}
	}
	
	public void createTable(){
		SQLiteDatabase dbCon = null;
		try{
		 dbCon = getWritableDatabase();
			if(null != dbCon){
				dbCon.execSQL(KlasseTbl.SQL_CREATE);
				dbCon.execSQL(VereinTbl.SQL_CREATE);
				dbCon.execSQL(MannschaftTbl.SQL_CREATE);
				dbCon.execSQL(SpielerTbl.SQL_CREATE);
				dbCon.execSQL(ErgebnisTbl.SQL_CREATE);
				
				dbCon.close();
			}
		} finally {
			dbCon.close();
		}
	}
	
	public void deleteData(){
		SQLiteDatabase dbCon = null;
		try{
		 dbCon = getWritableDatabase();
			if(null != dbCon){
				dbCon.execSQL(ErgebnisTbl.STMT_DELETE);
				dbCon.execSQL(SpielerTbl.STMT_SPIELER_DELETE);
				dbCon.execSQL(MannschaftTbl.STMT_MANNSCHAFT_DELETE);
				dbCon.execSQL(VereinTbl.STMT_VEREIN_DELETE);
				dbCon.execSQL(KlasseTbl.STMT_KLASSE_DELETE);
				
				dbCon.close();
			}
		} finally {
			dbCon.close();
		}
	}
	
	public void resetAutoincrements(){
		try{
			 db = getWritableDatabase();
				if(null != db){
					try {
						db.beginTransaction();
						
						final SQLiteStatement stmtResetKLasse = db.compileStatement("delete from sqlite_sequence;");						
						stmtResetKLasse.execute();
						db.setTransactionSuccessful();
						// CHECKSTYLE:OFF - methode deprecated
					} catch (Throwable ex) {
						// CHECKSTYLE:ON
						Log.e(TAG, "Fehler beim Reset des Autoincrements. " + ex);
					} finally {
						db.endTransaction();
					}

				}
			} finally {
				db.close();
			}
	}
	
	public void insertData(){
		try{
		 db = getWritableDatabase();
			if(null != db){
				erzeugeKlassenDaten(db, mContext, false);
				erzeugeVereinDaten(db, false);
				erzeugeMannschaftDaten(db, false);
				erzeugeSpielerDaten(db, false);
				erzeugeErgebnisDaten(db, false);
			}
		} finally {
			db.close();
		}
	}

	/**
	 * Wird aufgerufen, wenn das Datenbankschema neu angelegt werden soll. <br>
	 * Es wird die Tabelle <code>GeoKontaktTbl</code> angelegt. <br>
	 * Anschließend wird die Initialbefüllung der Datenbank durchgeführt.
	 * 
	 * @param db
	 *            Aktuelle Datenbank-Verbindung
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		this.db = db;
		initDatabase(mContext);
	}

	public static final void initDatabase(Context mContext) {
		db.execSQL(KlasseTbl.SQL_CREATE);
		db.execSQL(VereinTbl.SQL_CREATE);
		db.execSQL(MannschaftTbl.SQL_CREATE);
		db.execSQL(SpielerTbl.SQL_CREATE);
		db.execSQL(ErgebnisTbl.SQL_CREATE);

		erzeugeKlassenDaten(db, mContext, true);
		erzeugeVereinDaten(db, true);
		erzeugeMannschaftDaten(db, true);
		erzeugeSpielerDaten(db, true);
		erzeugeErgebnisDaten(db, true);
	}

	/**
	 * Wird aufgerufen, wenn sich die Version des Schemas geändert hat. <br>
	 * In diesem Fall wird die Datenbank gelöscht und mit neuem Schema wieder
	 * aufgebaut.
	 * 
	 * 
	 * @param db
	 *            Aktuelle Datenbank-Verbindung
	 * @param oldVersion
	 *            bisherige Schemaversion
	 * @param newVersion
	 *            neue Schemaversion
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		db.execSQL(KlasseTbl.SQL_DROP);
		onCreate(db);
	}

	/**
	 * Hier werden die Daten eingelesen. In diesem Fall werden die Datensätze
	 * zufällig erzeugt. <br>
	 * <strong>Die Methode sollte bald überflüssig werden </strong>
	 * 
	 * @param db
	 *            Aktuelle Datenbank-Verbindung.
	 * @param anzahlExemplare
	 *            Anzahl Testdatensätze.
	 * 
	 */
	private static void erzeugeKlassenDaten(SQLiteDatabase db, Context mContext, boolean deleteOldData) {

		// loesche vorhandene daten
		if(deleteOldData){
			db.delete(KlasseTbl.TABLE_NAME, null, null);
		}
		
		// init testdata
		final long t0 = System.currentTimeMillis();

		final SQLiteStatement stmtInsertKontakt = db.compileStatement(KlasseTbl.STMT_KLASSE_INSERT);

		String[] klassen = mContext.getResources().getStringArray(R.array.kegelklassen);
		db.beginTransaction();

		try {
			for (int r = 0; r < klassen.length; r++) {
				stmtInsertKontakt.bindString(1, klassen[r]);
				stmtInsertKontakt.executeInsert();
			}
			db.setTransactionSuccessful();
			// CHECKSTYLE:OFF - methode deprecated
		} catch (Throwable ex) {
			// CHECKSTYLE:ON
			Log.e(TAG, "Fehler beim Einf�gen eines Testdatensatzes. " + ex);
		} finally {
			db.endTransaction();
		}
		Log.w(TAG, "Importdauer Testdaten [ms] " + (System.currentTimeMillis() - t0));
	}

	/**
	 * Hier werden die Daten eingelesen. In diesem Fall werden die Datensätze
	 * zufällig erzeugt. <br>
	 * <strong>Die Methode sollte bald überflüssig werden </strong>
	 * 
	 * @param db
	 *            Aktuelle Datenbank-Verbindung.
	 * @param anzahlExemplare
	 *            Anzahl Testdatensätze.
	 * 
	 */
	private static void erzeugeMannschaftDaten(SQLiteDatabase db, boolean deleteOldData) {

		// loesche vorhandene daten
		if(deleteOldData){
			db.delete(MannschaftTbl.TABLE_NAME, null, null);
		}

		// init testdata
		final long t0 = System.currentTimeMillis();

		final SQLiteStatement stmtInsertKontakt = db.compileStatement(MannschaftTbl.STMT_NAME_KLASSE_MANNSCHAFT_INSERT);

		db.beginTransaction();

		try {
			KlassenGenerator.erzeugeBezirksoberliga(stmtInsertKontakt);
			KlassenGenerator.erzeugeBezirksliga(stmtInsertKontakt);
			KlassenGenerator.erzeugeBezirksklasse(stmtInsertKontakt);
			KlassenGenerator.erzeugeKreisliga(stmtInsertKontakt);
			KlassenGenerator.erzeugeKreisklasse(stmtInsertKontakt);
			KlassenGenerator.erzeugeAKlasse(stmtInsertKontakt);
			KlassenGenerator.erzeugeBKlasse(stmtInsertKontakt);
			KlassenGenerator.erzeugeCKlasse(stmtInsertKontakt);

			db.setTransactionSuccessful();
			// CHECKSTYLE:OFF - methode deprecated
		} catch (Throwable ex) {
			// CHECKSTYLE:ON
			Log.e(TAG, "Fehler beim Einf�gen eines Testdatensatzes. " + ex);
		} finally {
			db.endTransaction();
		}
		Log.w(TAG, "Importdauer Testdaten [ms] " + (System.currentTimeMillis() - t0));
	}

	/**
	 * Hier werden die Daten eingelesen. In diesem Fall werden die Datensätze
	 * zufällig erzeugt. <br>
	 * <strong>Die Methode sollte bald überflüssig werden </strong>
	 * 
	 * @param db
	 *            Aktuelle Datenbank-Verbindung.
	 * @param anzahlExemplare
	 *            Anzahl Testdatensätze.
	 * 
	 */
	private static void erzeugeSpielerDaten(SQLiteDatabase db, boolean deleteOldData) {

		// loesche vorhandene daten
		if(deleteOldData){
			db.delete(SpielerTbl.TABLE_NAME, null, null);
		}

		// init testdata
		final long t0 = System.currentTimeMillis();

		final SQLiteStatement stmtInsertKontakt = db.compileStatement(SpielerTbl.STMT_ALL_INSERT);

		db.beginTransaction();

		try {
			stmtInsertKontakt.bindLong(1, 53);
			stmtInsertKontakt.bindLong(2, 123);
			stmtInsertKontakt.bindString(3, "Platzer");
			stmtInsertKontakt.bindString(4, "G�nter");
			stmtInsertKontakt.bindString(5, "13.04.1972");
			stmtInsertKontakt.bindLong(6, (int) (48.17968 * 1E6));
			stmtInsertKontakt.bindLong(6, (int) (11.5922 * 1E6));
			stmtInsertKontakt.executeInsert();
			
			stmtInsertKontakt.bindLong(1, 53);
			stmtInsertKontakt.bindLong(2, 456);
			stmtInsertKontakt.bindString(3, "Arras");
			stmtInsertKontakt.bindString(4, "Klaus");
			stmtInsertKontakt.bindString(5, "12.09.1960");
			stmtInsertKontakt.executeInsert();
			
			/*
			
			stmtInsertKontakt.bindString(1, "Schulze Frank");
			stmtInsertKontakt.bindLong(2, 53);
			stmtInsertKontakt.executeInsert();
			
			stmtInsertKontakt.bindString(1, "Geldmacher Daniel");
			stmtInsertKontakt.bindLong(2, 53);
			stmtInsertKontakt.executeInsert();
			stmtInsertKontakt.bindString(1, "Staub Christoph");
			stmtInsertKontakt.bindLong(2, 53);
			stmtInsertKontakt.executeInsert();
			
			stmtInsertKontakt.bindString(1, "Frantz Hermann");
			stmtInsertKontakt.bindLong(2, 53);
			stmtInsertKontakt.executeInsert();
			
			stmtInsertKontakt.bindString(1, "Klostermaier G�nter");
			stmtInsertKontakt.bindLong(2, 53);
			stmtInsertKontakt.executeInsert();
			stmtInsertKontakt.bindString(1, "R�ttinger Markus");
			stmtInsertKontakt.bindLong(2, 53);
			stmtInsertKontakt.executeInsert();
			stmtInsertKontakt.bindString(1, "Steinbei�er Leonhard");
			stmtInsertKontakt.bindLong(2, 53);
			stmtInsertKontakt.executeInsert();

			stmtInsertKontakt.bindString(1, "Klostermaier Ralph");
			stmtInsertKontakt.bindLong(2, 15);
			stmtInsertKontakt.executeInsert();
			stmtInsertKontakt.bindString(1, "Kraus Johann");
			stmtInsertKontakt.bindLong(2, 15);
			stmtInsertKontakt.executeInsert();
			stmtInsertKontakt.bindString(1, "Daimer Max");
			stmtInsertKontakt.bindLong(2, 15);
			stmtInsertKontakt.executeInsert();
			stmtInsertKontakt.bindString(1, "Wettlaufer Fred");
			stmtInsertKontakt.bindLong(2, 15);
			stmtInsertKontakt.executeInsert();
			stmtInsertKontakt.bindString(1, "Binzer Klaus");
			stmtInsertKontakt.bindLong(2, 15);
			stmtInsertKontakt.executeInsert();
			
			*/

			db.setTransactionSuccessful();
			// CHECKSTYLE:OFF - methode deprecated
		} catch (Throwable ex) {
			// CHECKSTYLE:ON
			Log.e(TAG, "Fehler beim Einf�gen eines Testdatensatzes. " + ex);
		} finally {
			db.endTransaction();
		}
		Log.w(TAG, "Importdauer Testdaten [ms] " + (System.currentTimeMillis() - t0));
	}

	/**
	 * Hier werden die Daten eingelesen. In diesem Fall werden die Datensätze
	 * zufällig erzeugt. <br>
	 * <strong>Die Methode sollte bald überflüssig werden </strong>
	 * 
	 * @param db
	 *            Aktuelle Datenbank-Verbindung.
	 * @param anzahlExemplare
	 *            Anzahl Testdatensätze.
	 * 
	 */
	private static void erzeugeVereinDaten(SQLiteDatabase db,boolean deleteOldData) {

		// loesche vorhandene daten
		if(deleteOldData){
			db.delete(VereinTbl.TABLE_NAME, null, null);
		}

		// init testdata
		final long t0 = System.currentTimeMillis();

		final SQLiteStatement stmtInsertKontakt = db.compileStatement(VereinTbl.STMT_VEREIN_INSERT);

		db.beginTransaction();

		try {
			stmtInsertKontakt.bindString(1, "KC-Ismaning");
			stmtInsertKontakt.executeInsert();

			db.setTransactionSuccessful();
			// CHECKSTYLE:OFF - methode deprecated
		} catch (Throwable ex) {
			// CHECKSTYLE:ON
			Log.e(TAG, "Fehler beim Einf�gen eines Testdatensatzes. " + ex);
		} finally {
			db.endTransaction();
		}
		Log.w(TAG, "Importdauer Testdaten [ms] " + (System.currentTimeMillis() - t0));
	}
	
	/**
	 * Hier werden die Daten eingelesen. In diesem Fall werden die Datensätze
	 * zufällig erzeugt. <br>
	 * <strong>Die Methode sollte bald überflüssig werden </strong>
	 * 
	 * @param db
	 *            Aktuelle Datenbank-Verbindung.
	 * @param anzahlExemplare
	 *            Anzahl Testdatensätze.
	 * 
	 */
	private static void erzeugeErgebnisDaten(SQLiteDatabase db, boolean deleteOldData) {

		// loesche vorhandene daten
		if(deleteOldData){
			db.delete(ErgebnisTbl.TABLE_NAME, null, null);
		}

		// init testdata
		final long t0 = System.currentTimeMillis();

		final SQLiteStatement stmtInsertKontakt = db.compileStatement(ErgebnisTbl.STMT_MAX_INSERT);

		db.beginTransaction();

		try {
			//KlassenGenerator.erzeugeBezirksoberliga(stmtInsertKontakt);
			stmtInsertKontakt.bindLong(1, 1);
			stmtInsertKontakt.bindLong(2, 1);
			stmtInsertKontakt.bindLong(3, 1);
			stmtInsertKontakt.bindLong(4, 457);
			
			stmtInsertKontakt.bindLong(5, 230);
			stmtInsertKontakt.bindLong(6, 227);
			
			stmtInsertKontakt.bindLong(7, 160);
			stmtInsertKontakt.bindLong(8, 150);
			
			stmtInsertKontakt.bindLong(9, 70);
			stmtInsertKontakt.bindLong(10, 77);
			
			stmtInsertKontakt.bindLong(11, 1);
			stmtInsertKontakt.bindLong(12, 0);
			
			stmtInsertKontakt.executeInsert();

			db.setTransactionSuccessful();
			// CHECKSTYLE:OFF - methode deprecated
		} catch (Throwable ex) {
			// CHECKSTYLE:ON
			Log.e(TAG, "Fehler beim Einf�gen eines Testdatensatzes. " + ex);
		} finally {
			db.endTransaction();
		}
		Log.w(TAG, "Importdauer Testdaten [ms] " + (System.currentTimeMillis() - t0));
	}

}
