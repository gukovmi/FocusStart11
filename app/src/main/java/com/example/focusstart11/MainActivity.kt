package com.example.focusstart11

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var textObservable: Observable<String>
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fileName = "text.txt"
        val fileString: String = application.assets.open(fileName).bufferedReader()
            .use {
                it.readText()
            }
        textTextView.text = fileString

        textObservable = Observable
            .fromIterable(
                fileString.split(
                    "\n",
                    " ",
                    ".",
                    ",",
                    ";",
                    ":",
                    "!",
                    "?",
                    ignoreCase = true
                )
            )

        val searchObservable = RxEditTextObservable.fromView(searchingFieldEditText)
        compositeDisposable.add(
            searchObservable
                .debounce(700, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .subscribe({
                    find(it)
                }, {
                    Log.e("searchObservable", it.localizedMessage)
                })
        )
    }

    private fun find(text: String) {
        var count = 0
        compositeDisposable.add(
            textObservable
                .subscribeOn(Schedulers.io())
                .filter {
                    (text.isNotEmpty() && it.contains(text, ignoreCase = true))
                }
                .map {
                    ++count
                }
                .startWith(count)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    searchingCountTextView.text =
                            //this.getString(R.string.found_text_view, count.toString())
                        "Found : ${count}"
                }, {
                    Log.e("textObservable", it.localizedMessage)
                })
        )
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}