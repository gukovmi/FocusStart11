package com.example.focusstart11

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import io.reactivex.Observable
import io.reactivex.ObservableEmitter

class RxEditTextObservable {

    companion object {
        fun fromView(editText: EditText): Observable<String> =

            Observable.create { emitter: ObservableEmitter<String> ->
                val watcher = object : TextWatcher {
                    override fun afterTextChanged(p0: Editable?) {
                        if (!emitter.isDisposed) {
                            emitter.onNext(p0.toString())
                        }
                    }
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }
                }
                emitter.setCancellable {
                    editText.removeTextChangedListener(watcher)
                }
                editText.addTextChangedListener(watcher)
            }
    }
}