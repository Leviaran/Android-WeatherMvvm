package com.robotboy.weathermvvm.ui.activities

import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import butterknife.BindView
import com.jakewharton.rxbinding2.widget.textChanges
import com.robotboy.weathermvvm.R
import com.robotboy.weathermvvm.api.response.WeatherResponse
import com.robotboy.weathermvvm.helpers.hideKeyboard
import com.robotboy.weathermvvm.ui.base.BaseActivity
import com.robotboy.weathermvvm.viewmodel.WeatherViewModel

class MainActivity : BaseActivity() {
    override val layoutId = R.layout.activity_main
    @BindView(R.id.city) lateinit var cityEdit: EditText
    @BindView(R.id.temp) lateinit var tempView: TextView
    @BindView(R.id.request) lateinit var requestButton: Button
    private val model = WeatherViewModel()

    override fun bindToViewModel() {
        super.bindToViewModel()
        cityEdit.textChanges().map(CharSequence::toString).subscribe(model.city)
        requestButton.bindClicks(model.weatherRequest.triggerLoadingIndicator().doOnSubscribe { hideKeyboard(this) }, model.isInputValid).subscribe({showWeather(it)}, { showError(it) })
    }

    private fun showWeather(response: WeatherResponse) {
        val weather = response.main!!
        tempView.text = weather.tempCelsius
    }
}
