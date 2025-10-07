package viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ModeViewModel : ViewModel() {
    enum class Mode {
        GENERATE, PARSE
    }

    val mode: MutableLiveData<Mode> = MutableLiveData(Mode.GENERATE)
}