package profile

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class CustomProfileTextWatcher(
    private val mEditText: EditText,
    private val initialText: String,
    private val listener: EditTextWatcherDelegate
) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if ("${mEditText.text}" != initialText) {
            listener.enableSaveButton()
        } else {
            listener.disableSaveButton()
        }
    }

    override fun afterTextChanged(s: Editable) {}
}