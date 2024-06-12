package hr.foi.air.kontakti

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.provider.ContactsContract
import hr.foi.air.kontakti.models.Contact

class ContactsProvider {

    @SuppressLint("Range")
    fun getContacts(
        contentResolver: ContentResolver
    ): List<Contact> {
        val contacts = mutableListOf<Contact>();
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        );

        if (cursor!!.count > 0) {
            while (cursor.moveToNext()) {
                contacts.add(
                    Contact(
                        id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NAME_RAW_CONTACT_ID)),
                        name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),
                        phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    )
                )
            }
        }

        cursor.close();
        return contacts;
    }
}