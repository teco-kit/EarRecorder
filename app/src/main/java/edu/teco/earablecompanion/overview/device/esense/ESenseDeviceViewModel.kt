package edu.teco.earablecompanion.overview.device.esense

import android.bluetooth.BluetoothDevice
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import edu.teco.earablecompanion.bluetooth.ConnectionRepository
import kotlinx.coroutines.flow.collectLatest

class ESenseDeviceViewModel @ViewModelInject constructor(
    private val connectionRepository: ConnectionRepository,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val bluetoothDevice = savedStateHandle.get<BluetoothDevice>("device")
    val device: LiveData<ESenseDeviceItem> = liveData {
        val name = bluetoothDevice?.name ?: "Unknown device"
        connectionRepository.deviceConfigs.collectLatest {
            val config = it[bluetoothDevice?.address] as? ESenseConfig ?: ESenseConfig()
            emit(ESenseDeviceItem(name, config))
        }
    }

    fun setSampleRate(rate: Float) = connectionRepository.updateConfig(bluetoothDevice?.address) { (this as? ESenseConfig)?.sampleRate = rate.toInt() }

    fun setAccelerometerEnabled(enabled: Boolean) = connectionRepository.updateConfig(bluetoothDevice?.address) { (this as? ESenseConfig)?.accEnabled = enabled }
    fun setAccelerometerRange(range: ESenseConfig.AccRange) = connectionRepository.updateConfig(bluetoothDevice?.address) { (this as? ESenseConfig)?.accRange = range }
    fun setAccelerometerLPFBandwidth(bandwidth: ESenseConfig.AccLPF) = connectionRepository.updateConfig(bluetoothDevice?.address) { (this as? ESenseConfig)?.accLPF = bandwidth }

    fun setGyroSensorEnabled(enabled: Boolean) = connectionRepository.updateConfig(bluetoothDevice?.address) { (this as? ESenseConfig)?.gyroEnabled = enabled }
    fun setGyroSensorRange(range: ESenseConfig.GyroRange) = connectionRepository.updateConfig(bluetoothDevice?.address) { (this as? ESenseConfig)?.gyroRange = range }
    fun setGyroSensorLPFBandwidth(bandwidth: ESenseConfig.GyroLPF) = connectionRepository.updateConfig(bluetoothDevice?.address) { (this as? ESenseConfig)?.gyroLPF = bandwidth }
}