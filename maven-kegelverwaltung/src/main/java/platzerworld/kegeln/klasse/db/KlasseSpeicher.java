/**
 * 
 */
package platzerworld.kegeln.klasse.db;

import java.util.ArrayList;
import java.util.List;

import platzerworld.kegeln.KegelverwaltungDatenbank;
import platzerworld.kegeln.common.KeyValueVO;
import platzerworld.kegeln.common.db.Sortierung;
import platzerworld.kegeln.klasse.vo.KlasseVO;
import platzerworld.kegeln.mannschaft.db.MannschaftTbl;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

/**
 * Der KlasseSpeicher ist die Schnittstelle zu persistenten Klassendaten.
 * 
 * Die Klassendaten sind in der Anwendungsdatenbank abgelegt. Die Anwendung
 * sollte nur �ber den KlasseSpeicher auf gespeicherte Klassen zugreifen.
 * 
 * Um den KlasseSpeicher erzeugen zu k�nnen, muss die aufrufende
 * Android-Komponente ihren Context �bergeben.
 * 
 * @author platzerg
 */
public class KlasseSpeicher {

	/** Markierung f�r Logging. */
	private static final String TAG = "KlasseSpeicher";

	private static final String WHERE_NAME_LIKE = KlasseTbl.NAME + " LIKE ?";

	/** Verweis auf die Kegelverwaltung-Datenbank. */
	private KegelverwaltungDatenbank mDb;
	
	/**
	 * Erzeugt einen neuen KlasseSpeicher. 
	 * Dabei wird sichergestellt, dass die zugrundeliegende Datenbank unmittelbar nutzbar ist.
	 * 
	 * @param context  Kontext der Anwendung, f�r die der Speicher g�ltig sein soll.
	 */
	public KlasseSpeicher(Context context) {
		mDb = new KegelverwaltungDatenbank(context);
		Log.d(TAG, "KlasseSpeicher angelegt.");
	}

	/**
	 * Erzeugung ohne Context nicht m�glich.
	 */
	private KlasseSpeicher() {
	}

	/**
	 * Legt eine neue Klasse in der Datenbank an. Wenn das stichwort  gesetzt wird, werden auch 
	 * die Positionsangaben gespeichert.
	 * 
	 * @param name vollst�ndiger Name (Pflichtfeld)
	 * @return Datenbank-Id des neuen Kontakts
	 * @throws SQLException falls Speichern nicht m�glich.
	 */
	public long speichereKlasse(String name) {
		final ContentValues daten = new ContentValues();
		daten.put(KlasseTbl.NAME, name);

		final SQLiteDatabase dbCon = mDb.getWritableDatabase();

		try {
			final long id = dbCon.insertOrThrow(KlasseTbl.TABLE_NAME, null, daten);
			Log.i(TAG, "Klasse mit id=" + id + " erzeugt.");
			return id;
		} finally {
			dbCon.close();
		}
	}
	
	/**
	 * Legt eine neue Klasse in der Datenbank an. Wenn das stichwort  gesetzt wird, werden auch 
	 * die Positionsangaben gespeichert.
	 * 
	 * @param name vollst�ndiger Name (Pflichtfeld)
	 * @return Datenbank-Id des neuen Kontakts
	 * @throws SQLException falls Speichern nicht m�glich.
	 */
	public long speichereKlasse(KlasseVO klasseVO) {
		return this.speichereKlasse(klasseVO.name);
	}

	/**
	 * �ndert eine vorhandene Klasse in der Datenbank. Wenn die id nicht mitgegeben wird, wird keine �nderung durchgef�hrt.
	 * Es werden bei der �nderung alle Parameter ber�cksichtigt. 
	 * 
	 * @param id Schl�ssel des DB-Datensatzes.
	 * @param name vollst�ndiger Name (Pflichtfeld)
	 */
	public void aendereKlasse(long id, String name) {
		if (id == 0) {
			Log.w(TAG, "id == 0 => kein update m�glich.");
			return;
		}

		final ContentValues daten = new ContentValues();
		daten.put(KlasseTbl.NAME, name);

		final SQLiteDatabase dbCon = mDb.getWritableDatabase();

		try {
			dbCon.update(KlasseTbl.TABLE_NAME, daten, KlasseTbl.WHERE_ID_EQUALS, new String[] { String.valueOf(id) });
			Log.i(TAG, "Klasse id=" + id + " aktualisiert.");
		} finally {
			dbCon.close();
		}
	}

	/**
	 * Entfernt eine Klasse aus der Datenbank.
	 * 
	 * @param id Schl�ssel des gesuchten Kontakts
	 * @return true, wenn Datensatz geloescht wurde.
	 */
	public boolean loescheKlasse(long id) {
		final SQLiteDatabase dbCon = mDb.getWritableDatabase();

		int anzahlLoeschungen = 0;
		try {
			anzahlLoeschungen = dbCon.delete(KlasseTbl.TABLE_NAME, KlasseTbl.WHERE_ID_EQUALS,
					new String[] { String.valueOf(id) });
			Log.i(TAG, "Klasse id=" + id + " gelöscht.");
		} finally {
			dbCon.close();
		}
		return anzahlLoeschungen == 1;
	}

	/**
	 * Liefert einen Cursor auf alle Felder der Klasse-Tabelle zur�ck.
	 * Wenn eine komplette Klasse genutzt werden soll, ist die ladeKlasse-Methode vorzuziehen.
	 * 
	 * @param id Schl�ssel des gesuchten Kontakts
	 * @return Cursor, oder null
	 */
	public Cursor ladeKlasseDetails(long id) {
		return mDb.getReadableDatabase().query(KlasseTbl.TABLE_NAME, KlasseTbl.ALL_COLUMNS, KlasseTbl.WHERE_ID_EQUALS,
				new String[] { String.valueOf(id) }, null, null, null);
	}

	/**
	 * Liefert ein Value-Object KlasseVO mit allen Feldern der Klasse-Tabelle zur�ck.
	 * 
	 * @param id Schl�ssel des gesuchten Kontakts
	 * @return Cursor, oder null
	 */
	public KlasseVO ladeKlasse(long id) {
		KlasseVO kontakt = null;
		Cursor c = null;
		try {
			c = mDb.getReadableDatabase().query(KlasseTbl.TABLE_NAME, KlasseTbl.ALL_COLUMNS, KlasseTbl.WHERE_ID_EQUALS,
					new String[] { String.valueOf(id) }, null, null, null);
			if (c.moveToFirst() == false) {
				return null;
			}
			kontakt = ladeKlasseVO(c);
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return kontakt;
	}

	/**
	 * Liefert alle Kontakte sortiert nach NAME zur�ck. 
	 * Es kann (optional) ein Filterkriterium angegeben werden. 
	 * Wenn der namensFilter definiert ist, werden nur Klassen geliefert, deren NAME mit diesem Buchstaben beginnt.
	 * 
	 * @param namensFilter Anfangsbuchstaben (case sensitive) der zu suchenden Klasse.
	 * @return Cursor auf die Ergebnisliste.
	 */
	public Cursor ladeKlassenListe(CharSequence namensFilter) {
		return ladeKlassenListe(Sortierung.ID, namensFilter);
	}

	/**
	 * Liefert alle Klassen mit einstellbarer Sortierung zur�ck. 
	 * Es kann (optional) ein Filterkriterium angegeben werden. 
	 * Wenn der namensFilter definiert ist, werden nur Klassen geliefert, deren NAME mit diesem Buchstaben beginnt.
	 * 
	 * @param sortierung Art der Sortierung
	 * @param namensFilter Anfangsbuchstaben (case sensitive) der zu suchenden Kontakte.
	 * @return Cursor auf die Ergebnisliste.
	 */
	public Cursor ladeKlassenListe(Sortierung sortierung, CharSequence namensFilter) {
		try{
			final SQLiteQueryBuilder kontaktSuche = new SQLiteQueryBuilder();
			kontaktSuche.setTables(KlasseTbl.TABLE_NAME);
			String[] whereAttribs = null;
			if (namensFilter != null && namensFilter.length() > 0) {
				kontaktSuche.appendWhere(WHERE_NAME_LIKE);
				whereAttribs = new String[] { namensFilter + "%" };
			}
			Cursor klassenCursor = kontaktSuche.query(mDb.getReadableDatabase(), KlasseTbl.ALL_COLUMNS, null, whereAttribs, null, null,
					getKontaktSortierung(sortierung));
			
			
			
			boolean n = klassenCursor.moveToFirst();
			
			return klassenCursor;
		}finally{
			schliessen();
		}
	}
	
	/**
	 * Liefert alle Kontakte sortiert nach NAME zur�ck. 
	 * Es kann (optional) ein Filterkriterium angegeben werden. 
	 * Wenn der namensFilter definiert ist, werden nur Klassen geliefert, deren NAME mit diesem Buchstaben beginnt.
	 * 
	 * @return Cursor auf die Ergebnisliste.
	 */
	public List<KeyValueVO> ladeAlleKlassenListeVO() {
		return ladeKlassenListeVO(Sortierung.STANDARD, null);
	}
	
	/**
	 * Liefert alle Kontakte sortiert nach NAME zur�ck. 
	 * Es kann (optional) ein Filterkriterium angegeben werden. 
	 * Wenn der namensFilter definiert ist, werden nur Klassen geliefert, deren NAME mit diesem Buchstaben beginnt.
	 * 
	 * @param namensFilter Anfangsbuchstaben (case sensitive) der zu suchenden Klasse.
	 * @return Cursor auf die Ergebnisliste.
	 */
	public List<KeyValueVO> ladeKlassenListeVO(CharSequence namensFilter) {
		return ladeKlassenListeVO(Sortierung.STANDARD, namensFilter);
	}
	
	/**
	 * Liefert alle Klassen mit einstellbarer Sortierung zur�ck. 
	 * Es kann (optional) ein Filterkriterium angegeben werden. 
	 * Wenn der namensFilter definiert ist, werden nur Klassen geliefert, deren NAME mit diesem Buchstaben beginnt.
	 * 
	 * @param sortierung Art der Sortierung
	 * @param namensFilter Anfangsbuchstaben (case sensitive) der zu suchenden Kontakte.
	 * @return Cursor auf die Ergebnisliste.
	 */
	public List<KeyValueVO> ladeKlassenListeVO(Sortierung sortierung, CharSequence namensFilter) {
		try{
			final SQLiteQueryBuilder kontaktSuche = new SQLiteQueryBuilder();
			kontaktSuche.setTables(KlasseTbl.TABLE_NAME);
			String[] whereAttribs = null;
			if (namensFilter != null && namensFilter.length() > 0) {
				kontaktSuche.appendWhere(WHERE_NAME_LIKE);
				whereAttribs = new String[] { namensFilter + "%" };
			}
			Cursor klassenCursor = kontaktSuche.query(mDb.getReadableDatabase(), KlasseTbl.ALL_COLUMNS, null, whereAttribs, null, null,
					getKontaktSortierung(sortierung));
			
			
			return ladeKlasseKeyValueVO(klassenCursor);
		}finally{
			schliessen();
		}
	}

	/**
	 * Liefert die Sortierung unter Ber�cksichtigung der Standard-Sortierung der Klassen-Tabelle.
	 * 
	 * @param sortierung Sortierung als enum.
	 * @return Sortierung als ORDER_BY kompatible Anweisung.
	 */
	private static String getKontaktSortierung(Sortierung sortierung) {
		String sortiertNach = KlasseTbl.DEFAULT_SORT_ORDER;
		switch (sortierung) {
		case NAME:
			sortiertNach = KlasseTbl.NAME;
			break;
		case ID:
			sortiertNach = MannschaftTbl.ID;
			break;
		default:
			break;
		}
		return sortiertNach;
	}

	/**
	 * L�dt die Klassen aus dem KlasseTbl-Datensatz, auf dem der Cursor gerade steht.
	 * 
	 * Der Cursor wird anschlie�end deaktiviert, da er im KlasseSpeicher nur intern als "letzter Aufruf" aufgerufen wird.
	 * 
	 * @param c aktuelle Cursorposition != null
	 * @return Exemplar von Klasse.
	 */
	public KlasseVO ladeKlasseVO(Cursor c) {
		final KlasseVO kontakt = new KlasseVO();

		kontakt.id = c.getLong(c.getColumnIndex(KlasseTbl.ID));
		kontakt.name = c.getString(c.getColumnIndex(KlasseTbl.NAME));

		return kontakt;
	}
	
	/**
	 * L�dt die Klassen aus dem KlasseTbl-Datensatz, auf dem der Cursor gerade steht.
	 * 
	 * Der Cursor wird anschlie�end deaktiviert, da er im KlasseSpeicher nur intern als "letzter Aufruf" aufgerufen wird.
	 * 
	 * @param c aktuelle Cursorposition != null
	 * @return Exemplar von Klasse.
	 */
	public ArrayList<KeyValueVO> ladeKlasseKeyValueVO(Cursor c) {	
		ArrayList<KeyValueVO> klassenVOs = new ArrayList<KeyValueVO>();
		KeyValueVO klasseVO = null;
		if (c != null ) {
    		if  (c.moveToFirst()) {
    			do {    				
    				klasseVO = new KeyValueVO(
    						c.getLong(c.getColumnIndex(KlasseTbl.ID)), 
    						c.getString(c.getColumnIndex(KlasseTbl.NAME)));
    				
    				klassenVOs.add(klasseVO);
    			}while (c.moveToNext());
    		} 
    	}

		return klassenVOs;
	}
	
	/**
	 * L�dt die Klassen aus dem KlasseTbl-Datensatz, auf dem der Cursor gerade steht.
	 * 
	 * Der Cursor wird anschlie�end deaktiviert, da er im KlasseSpeicher nur intern als "letzter Aufruf" aufgerufen wird.
	 * 
	 * @param c aktuelle Cursorposition != null
	 * @return Exemplar von Klasse.
	 */
	public ArrayList<KlasseVO> ladeKlassenAsKlasseVO() {	
		Cursor c = ladeKlassenListe(null);
		ArrayList<KlasseVO> klassenListe = new ArrayList<KlasseVO>();
		if (c != null ) {
    		if  (c.moveToFirst()) {
    			do { 
    				KlasseVO klasseVO = new KlasseVO(c.getInt(c.getColumnIndex(KlasseTbl.ID)),
    						c.getString(c.getColumnIndex(KlasseTbl.NAME)));
    				klassenListe.add(klasseVO);
    			}while (c.moveToNext());
    		} 
    	}

		return klassenListe;
	}
	
	

	/**
	 * Schliesst die zugrundeliegende Datenbank. Vor dem naechsten Zugriff muss
	 * oeffnen() aufgerufen werden.
	 */
	public void schliessen() {
		mDb.close();
		Log.d(TAG, "Datenbank amando geschlossen.");
	}

	/**
	 * Oeffnet die Datenbank, falls sie vorher mit schliessen() geschlossen
	 * wurde. <br>
	 * Bei Bedarf wird das Schema angelegt bzw. aktualisiert.
	 */
	public void oeffnen() {
		mDb.getReadableDatabase();
		Log.d(TAG, "Datenbank kegelverwaltung geoeffnet.");
	}
	
	/**
	 * Oeffnet die Datenbank schreibbar, falls sie vorher mit schliessen() geschlossen wurde.
	 * Bei Bedarf wird das Schema angelegt bzw. aktualisiert.
	 */
	public void oeffnenSchreibzugriff() {
		mDb.getWritableDatabase();
		Log.d(TAG, "Datenbank kegelverwaltung schreibend geoeffnet.");
	}

	/**
	 * Gibt die Anzahl der Klassen in der Datenbank zurueck. 
	 * Performanter als Cursor::getCount.
	 * 
	 * @return Anzahl der Klassen.
	 */
	public int anzahlKlassen() {
		final Cursor c = mDb.getReadableDatabase().rawQuery("select count(*) from " + KlasseTbl.TABLE_NAME, null);
		if (c.moveToFirst() == false) {
			return 0;
		}
		return c.getInt(0);
	}
}
