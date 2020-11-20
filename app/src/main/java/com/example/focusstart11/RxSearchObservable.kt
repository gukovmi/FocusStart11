package com.example.focusstart11

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class RxEditTextObservable {

    companion object {
        fun fromView(searchView: EditText): Observable<String> {

            val subject: PublishSubject<String> = PublishSubject.create()

            searchView.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(p0: Editable?) {
                    subject.onNext(p0.toString())
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
            })

            return subject
        }
    }
}