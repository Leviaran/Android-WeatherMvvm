package com.robotboy.weathermvvm.ui.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.robotboy.weathermvvm.helpers.MessageHelpers
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.Observable
import com.robotboy.weathermvvm.R
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

abstract class BaseActivity : RxAppCompatActivity() {
    @get:LayoutRes protected abstract val layoutId: Int
    @BindView(R.id.loader) lateinit var loaderView: View
    private lateinit var unbinder: Unbinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        unbinder = ButterKnife.bind(this)
        onViewBound()
        bindToViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        unbinder.unbind()
    }

    open fun onViewBound() {}

    open fun bindToViewModel() {}

    protected open fun showError(error: Throwable) {
        MessageHelpers.showExceptionError(this, error, null)
    }

    protected fun <T> Observable<T>.triggerLoadingIndicator(): Observable<T> = this
            .doOnSubscribe {
                loaderView.post {
                    loaderView.visibility = View.VISIBLE
                }
            }
            .doFinally {
                loaderView.post {
                    loaderView.visibility = View.INVISIBLE
                }
            }


    private fun <T> View.bindClicksWithErrors(action: Observable<T>, isEnabledByUser: Observable<Boolean>? = null): Observable<Observable<T>> {
        val executions = PublishSubject.create<Observable<T>>()
        val isExecuting = BehaviorSubject.createDefault<Boolean>(false)

        val isEnabled = if (isEnabledByUser != null) {
            val combineEnabled = BiFunction<Boolean, Boolean, Boolean> { isUserEnabled, isExec -> isUserEnabled && !isExec }
            Observable.combineLatest(isEnabledByUser.startWith(false), isExecuting, combineEnabled)
        } else {
            isExecuting.map { !it }
        }

        isEnabled.bindToLifecycle(this).subscribe(::setEnabled)

        setOnClickListener {
            val replay = action
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(::showError)
                    .doFinally { isExecuting.onNext(false) }
                    .replay()
            executions.onNext(replay)
            isExecuting.onNext(true)
            replay.connect()
        }
        return executions.bindToLifecycle(this)
    }

    fun <T> View.bindClicks(action: Observable<T>, isEnabledByUser: Observable<Boolean>? = null): Observable<T> {
        return bindClicksWithErrors(action, isEnabledByUser)
                .flatMap {
                    it.onErrorResumeNext { _: Throwable ->
                        Observable.empty()
                    }
                }
    }
}
