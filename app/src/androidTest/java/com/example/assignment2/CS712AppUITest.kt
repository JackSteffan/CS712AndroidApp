package com.example.assignment2

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CS712AppUITest {

    private lateinit var device: UiDevice
    private val appPackage = "com.example.assignment2"
    private val launchTimeout = 5000L

    @Before
    fun setUp() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        device.pressHome()
        device.wait(Until.hasObject(By.pkg(device.launcherPackageName).depth(0)), launchTimeout)

        val context = InstrumentationRegistry.getInstrumentation().context
        val intent = context.packageManager.getLaunchIntentForPackage(appPackage)
            ?: throw AssertionError("Could not find launch intent for $appPackage")
        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)

        device.wait(Until.hasObject(By.pkg(appPackage).depth(0)), launchTimeout)
    }

    @Test
    fun launchApp_navigateToSecondActivity_verifyChallengeText() {

        val allowButton = device.wait(
            Until.findObject(
                By.text("Allow")
                    .pkg("com.android.permission controller")
            ),
            3000L
        )
        allowButton?.click()


        if (allowButton == null) {
            val denyButton = device.findObject(By.text("Don't Allow"))
            denyButton?.click()
        }

        device.wait(Until.hasObject(By.pkg(appPackage).depth(0)), launchTimeout)


        val startButton = device.wait(
            Until.findObject(By.text("Second Activity EX")),
            launchTimeout
        )
        assertNotNull("Could not find 'Second Activity EX' button", startButton)
        startButton.click()


        device.wait(Until.hasObject(By.pkg(appPackage).depth(0)), launchTimeout)

        val challengeText = device.wait(
            Until.findObject(By.textContains("OS Version Compatibility")),
            launchTimeout
        )
        assertNotNull(
            "Expected a mobile software engineering challenge to be visible in the second activity",
            challengeText
        )
    }
}