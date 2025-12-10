package com.example.hh000_android_master_kotlin_xml.core.extentions

import android.accounts.AccountManager
import android.app.ActivityManager
import android.app.AlarmManager
import android.app.AppOpsManager
import android.app.DownloadManager
import android.app.GrammaticalInflectionManager
import android.app.KeyguardManager
import android.app.LocaleManager
import android.app.NotificationManager
import android.app.SearchManager
import android.app.UiModeManager
import android.app.WallpaperManager
import android.app.admin.DevicePolicyManager
import android.app.job.JobScheduler
import android.app.role.RoleManager
import android.app.usage.NetworkStatsManager
import android.app.usage.StorageStatsManager
import android.app.usage.UsageStatsManager
import android.appwidget.AppWidgetManager
import android.bluetooth.BluetoothManager
import android.companion.CompanionDeviceManager
import android.companion.virtual.VirtualDeviceManager
import android.content.ClipboardManager
import android.content.Context
import android.content.RestrictionsManager
import android.content.pm.CrossProfileApps
import android.content.pm.LauncherApps
import android.content.pm.PackageManager
import android.content.pm.ShortcutManager
import android.credentials.CredentialManager
import android.devicelock.DeviceLockManager
import android.graphics.drawable.Drawable
import android.hardware.ConsumerIrManager
import android.hardware.SensorManager
import android.hardware.biometrics.BiometricManager
import android.hardware.camera2.CameraManager
import android.hardware.display.DisplayManager
import android.hardware.input.InputManager
import android.hardware.usb.UsbManager
import android.health.connect.HealthConnectManager
import android.location.LocationManager
import android.media.AudioManager
import android.media.MediaCommunicationManager
import android.media.MediaRouter
import android.media.metrics.MediaMetricsManager
import android.media.midi.MidiManager
import android.media.projection.MediaProjectionManager
import android.media.session.MediaSessionManager
import android.media.tv.TvInputManager
import android.media.tv.interactive.TvInteractiveAppManager
import android.net.ConnectivityManager
import android.net.IpSecManager
import android.net.VpnManager
import android.net.nsd.NsdManager
import android.net.wifi.WifiManager
import android.net.wifi.aware.WifiAwareManager
import android.net.wifi.p2p.WifiP2pManager
import android.net.wifi.rtt.WifiRttManager
import android.nfc.NfcManager
import android.os.BatteryManager
import android.os.Build
import android.os.DropBoxManager
import android.os.HardwarePropertiesManager
import android.os.PowerManager
import android.os.SecurityStateManager
import android.os.UserManager
import android.os.VibratorManager
import android.os.health.SystemHealthManager
import android.os.storage.StorageManager
import android.print.PrintManager
import android.provider.E2eeContactKeysManager
import android.service.persistentdata.PersistentDataBlockManager
import android.telecom.TelecomManager
import android.telephony.CarrierConfigManager
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.telephony.euicc.EuiccManager
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.accessibility.AccessibilityManager
import android.view.accessibility.CaptioningManager
import android.view.displayhash.DisplayHashManager
import android.view.inputmethod.InputMethodManager
import android.view.textclassifier.TextClassificationManager
import android.view.textservice.TextServicesManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

object MyExtContext {
    //----------------------------------------------------------------------------------------------
    fun Context.getDimensionInDp0(@DimenRes dimenId: Int): Float {
        return this.resources.getDimension(dimenId)
    }

    fun Context.getColorInt0(@ColorRes colorID: Int): Int {
        return ContextCompat.getColor(this, colorID)
    }

    fun Context.getString0(@StringRes stringID: Int): String {
        return ContextCompat.getString(this, stringID)
    }

    fun Context.getDrawable0(@DrawableRes drawableID: Int): Drawable? {
        return ContextCompat.getDrawable(this, drawableID)
    }

    //----------------------------------------------------------------------------------------------
    fun Context.showToastShort0(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun Context.showToastLong0(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    //----------------------------------------------------------------------------------------------
    fun Context.isPermissionGranted0(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    //----------------------------------------------------------------------------------------------
    fun Context.getConnectivityManager0(): ConnectivityManager {
        return (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
    }

    fun Context.getPowerManager0(): PowerManager {
        return (getSystemService(Context.POWER_SERVICE) as PowerManager)
    }

    fun Context.getWindowManager0(): WindowManager {
        return (getSystemService(Context.WINDOW_SERVICE) as WindowManager)
    }

    fun Context.getLayoutInflater0(): LayoutInflater {
        return (getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
    }

    fun Context.getAccountManager0(): AccountManager {
        return (getSystemService(Context.ACCOUNT_SERVICE) as AccountManager)
    }

    fun Context.getActivityManager0(): ActivityManager {
        return (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
    }

    fun Context.getAlarmManager0(): AlarmManager {
        return (getSystemService(Context.ALARM_SERVICE) as AlarmManager)
    }

    fun Context.getNotificationManager0(): NotificationManager {
        return (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
    }

    fun Context.getAccessibilityManager0(): AccessibilityManager {
        return (getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager)
    }

    fun Context.getCaptioningManager0(): CaptioningManager {
        return (getSystemService(Context.CAPTIONING_SERVICE) as CaptioningManager)
    }

    fun Context.getKeyguardManager0(): KeyguardManager {
        return (getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager)
    }

    fun Context.getLocationManager0(): LocationManager {
        return (getSystemService(Context.LOCATION_SERVICE) as LocationManager)
    }

    fun Context.getSearchManager0(): SearchManager {
        return (getSystemService(Context.SEARCH_SERVICE) as SearchManager)
    }

    fun Context.getSensorManager0(): SensorManager {
        return (getSystemService(Context.SENSOR_SERVICE) as SensorManager)
    }

    fun Context.getStorageManager0(): StorageManager {
        return (getSystemService(Context.STORAGE_SERVICE) as StorageManager)
    }

    fun Context.getStorageStatsManager0(): StorageStatsManager {
        return (getSystemService(Context.STORAGE_STATS_SERVICE) as StorageStatsManager)
    }

    fun Context.getWallpaperManager0(): WallpaperManager {
        return (getSystemService(Context.WALLPAPER_SERVICE) as WallpaperManager)
    }

    fun Context.getNetworkStatsManager0(): NetworkStatsManager {
        return (getSystemService(Context.NETWORK_STATS_SERVICE) as NetworkStatsManager)
    }

    fun Context.getWifiManager0(): WifiManager {
        return (getSystemService(Context.WIFI_SERVICE) as WifiManager)
    }

    fun Context.getWifiAwareManager0(): WifiAwareManager {
        return (getSystemService(Context.WIFI_AWARE_SERVICE) as WifiAwareManager)
    }

    fun Context.getWifiP2pManager0(): WifiP2pManager {
        return (getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager)
    }

    fun Context.getNsdManager0(): NsdManager {
        return (getSystemService(Context.NSD_SERVICE) as NsdManager)
    }

    fun Context.getAudioManager0(): AudioManager {
        return (getSystemService(Context.AUDIO_SERVICE) as AudioManager)
    }

    fun Context.getMediaRouter0(): MediaRouter {
        return (getSystemService(Context.MEDIA_ROUTER_SERVICE) as MediaRouter)
    }

    fun Context.getTelephonyManager0(): TelephonyManager {
        return (getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager)
    }

    fun Context.getSubscriptionManager0(): SubscriptionManager {
        return (getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager)
    }

    fun Context.getCarrierConfigManager0(): CarrierConfigManager {
        return (getSystemService(Context.CARRIER_CONFIG_SERVICE) as CarrierConfigManager)
    }

    fun Context.getTelecomManager0(): TelecomManager {
        return (getSystemService(Context.TELECOM_SERVICE) as TelecomManager)
    }

    fun Context.getClipboardManager0(): ClipboardManager {
        return (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
    }

    fun Context.getInputMethodManager0(): InputMethodManager {
        return (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
    }

    fun Context.getTextServicesManager0(): TextServicesManager {
        return (getSystemService(Context.TEXT_SERVICES_MANAGER_SERVICE) as TextServicesManager)
    }

    fun Context.getTextClassificationManager0(): TextClassificationManager {
        return (getSystemService(Context.TEXT_CLASSIFICATION_SERVICE) as TextClassificationManager)
    }

    fun Context.getAppWidgetManager0(): AppWidgetManager {
        return (getSystemService(Context.APPWIDGET_SERVICE) as AppWidgetManager)
    }

    fun Context.getDropBoxManager0(): DropBoxManager {
        return (getSystemService(Context.DROPBOX_SERVICE) as DropBoxManager)
    }

    fun Context.getDevicePolicyManager0(): DevicePolicyManager {
        return (getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager)
    }

    fun Context.getUiModeManager0(): UiModeManager {
        return (getSystemService(Context.UI_MODE_SERVICE) as UiModeManager)
    }

    fun Context.getDownloadManager0(): DownloadManager {
        return (getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager)
    }

    fun Context.getNfcManager0(): NfcManager {
        return (getSystemService(Context.NFC_SERVICE) as NfcManager)
    }

    fun Context.getBluetoothManager0(): BluetoothManager {
        return (getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager)
    }

    fun Context.getUsbManager0(): UsbManager {
        return (getSystemService(Context.USB_SERVICE) as UsbManager)
    }

    fun Context.getLauncherApps0(): LauncherApps {
        return (getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps)
    }

    fun Context.getInputManager0(): InputManager {
        return (getSystemService(Context.INPUT_SERVICE) as InputManager)
    }

    fun Context.getDisplayManager0(): DisplayManager {
        return (getSystemService(Context.DISPLAY_SERVICE) as DisplayManager)
    }

    fun Context.getUserManager0(): UserManager {
        return (getSystemService(Context.USER_SERVICE) as UserManager)
    }

    fun Context.getRestrictionsManager0(): RestrictionsManager {
        return (getSystemService(Context.RESTRICTIONS_SERVICE) as RestrictionsManager)
    }

    fun Context.getAppOpsManager0(): AppOpsManager {
        return (getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager)
    }

    fun Context.getCameraManager0(): CameraManager {
        return (getSystemService(Context.CAMERA_SERVICE) as CameraManager)
    }

    fun Context.getPrintManager0(): PrintManager {
        return (getSystemService(Context.PRINT_SERVICE) as PrintManager)
    }

    fun Context.getConsumerIrManager0(): ConsumerIrManager {
        return (getSystemService(Context.CONSUMER_IR_SERVICE) as ConsumerIrManager)
    }

    fun Context.getTvInputManager0(): TvInputManager {
        return (getSystemService(Context.TV_INPUT_SERVICE) as TvInputManager)
    }

    fun Context.getUsageStatsManager0(): UsageStatsManager {
        return (getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager)
    }

    fun Context.getMediaSessionManager0(): MediaSessionManager {
        return (getSystemService(Context.MEDIA_SESSION_SERVICE) as MediaSessionManager)
    }

    fun Context.getBatteryManager0(): BatteryManager {
        return (getSystemService(Context.BATTERY_SERVICE) as BatteryManager)
    }

    fun Context.getJobScheduler0(): JobScheduler {
        return (getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler)
    }

    fun Context.getMediaProjectionManager0(): MediaProjectionManager {
        return (getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager)
    }

    fun Context.getMidiManager0(): MidiManager {
        return (getSystemService(Context.MIDI_SERVICE) as MidiManager)
    }

    fun Context.getHardwarePropertiesManager0(): HardwarePropertiesManager {
        return (getSystemService(Context.HARDWARE_PROPERTIES_SERVICE) as HardwarePropertiesManager)
    }

    fun Context.getShortcutManager0(): ShortcutManager {
        return (getSystemService(Context.SHORTCUT_SERVICE) as ShortcutManager)
    }

    fun Context.getSystemHealthManager0(): SystemHealthManager {
        return (getSystemService(Context.SYSTEM_HEALTH_SERVICE) as SystemHealthManager)
    }

    fun Context.getCompanionDeviceManager0(): CompanionDeviceManager {
        return (getSystemService(Context.COMPANION_DEVICE_SERVICE) as CompanionDeviceManager)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun Context.getIpSecManager0(): IpSecManager {
        return (getSystemService(Context.IPSEC_SERVICE) as IpSecManager)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun Context.getWifiRttManager0(): WifiRttManager {
        return (getSystemService(Context.WIFI_RTT_RANGING_SERVICE) as WifiRttManager)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun Context.getEuiccManager0(): EuiccManager {
        return (getSystemService(Context.EUICC_SERVICE) as EuiccManager)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun Context.getCrossProfileApps0(): CrossProfileApps {
        return (getSystemService(Context.CROSS_PROFILE_APPS_SERVICE) as CrossProfileApps)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun Context.getBiometricManager0(): BiometricManager {
        return (getSystemService(Context.BIOMETRIC_SERVICE) as BiometricManager)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun Context.getRoleManager0(): RoleManager {
        return (getSystemService(Context.ROLE_SERVICE) as RoleManager)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun Context.getVpnManager0(): VpnManager {
        return (getSystemService(Context.VPN_MANAGEMENT_SERVICE) as VpnManager)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun Context.getVibratorManager0(): VibratorManager {
        return (getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun Context.getMediaMetricsManager0(): MediaMetricsManager {
        return (getSystemService(Context.MEDIA_METRICS_SERVICE) as MediaMetricsManager)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun Context.getDisplayHashManager0(): DisplayHashManager {
        return (getSystemService(Context.DISPLAY_HASH_SERVICE) as DisplayHashManager)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun Context.getMediaCommunicationManager0(): MediaCommunicationManager {
        return (getSystemService(Context.MEDIA_COMMUNICATION_SERVICE) as MediaCommunicationManager)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun Context.getTvInteractiveAppManager0(): TvInteractiveAppManager {
        return (getSystemService(Context.TV_INTERACTIVE_APP_SERVICE) as TvInteractiveAppManager)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun Context.getLocaleManager0(): LocaleManager {
        return (getSystemService(Context.LOCALE_SERVICE) as LocaleManager)
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun Context.getVirtualDeviceManager0(): VirtualDeviceManager {
        return (getSystemService(Context.VIRTUAL_DEVICE_SERVICE) as VirtualDeviceManager)
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun Context.getCredentialManager0(): CredentialManager {
        return (getSystemService(Context.CREDENTIAL_SERVICE) as CredentialManager)
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun Context.getHealthConnectManager0(): HealthConnectManager {
        return (getSystemService(Context.HEALTHCONNECT_SERVICE) as HealthConnectManager)
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun Context.getDeviceLockManager0(): DeviceLockManager {
        return (getSystemService(Context.DEVICE_LOCK_SERVICE) as DeviceLockManager)
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun Context.getGrammaticalInflectionManager0(): GrammaticalInflectionManager {
        return (getSystemService(Context.GRAMMATICAL_INFLECTION_SERVICE) as GrammaticalInflectionManager)
    }

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    fun Context.getSecurityStateManager0(): SecurityStateManager {
        return (getSystemService(Context.SECURITY_STATE_SERVICE) as SecurityStateManager)
    }

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    fun Context.getE2eeContactKeysManager0(): E2eeContactKeysManager {
        return (getSystemService(Context.CONTACT_KEYS_SERVICE) as E2eeContactKeysManager)
    }

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    fun Context.getPersistentDataBlockManager0(): PersistentDataBlockManager {
        return (getSystemService(Context.PERSISTENT_DATA_BLOCK_SERVICE) as PersistentDataBlockManager)
    }
    //----------------------------------------------------------------------------------------------
}