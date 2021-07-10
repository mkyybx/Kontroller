package com.github.roarappstudio.btkontroller.senders


import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothHidDevice
import android.util.Log
import android.view.KeyEvent
import com.github.roarappstudio.btkontroller.reports.KeyboardReport
import java.util.concurrent.locks.Lock

@Suppress("MemberVisibilityCanBePrivate")
open class KeyboardSender(
    val hidDevice: BluetoothHidDevice,
    val host: BluetoothDevice,
    val lock: Lock
) {
    val keyboardReport = KeyboardReport()
    //val keyPosition : IntArray = IntArray(6){0}


    protected open fun sendKeys() {
        if (!hidDevice.sendReport(host, KeyboardReport.ID, keyboardReport.bytes)) {
            Log.e(TAG, "Report wasn't sent")
        }
    }

    protected open fun customSender(modifier_checked_state: Int) {
        sendKeys()
//        if (modifier_checked_state == 0) sendNullKeys()
//        else {
//            keyboardReport.key1 = 0.toByte()
//            sendKeys()
//        }
    }

    protected open fun setKeys(event: KeyEvent): Boolean {
        val keyDown = event.action == KeyEvent.ACTION_DOWN
        var changed = false
        val keyCode = KeyboardReport.KeyEventMap[event.keyCode]
        // deal with special keys
        when (event.keyCode) {
            KeyEvent.KEYCODE_CTRL_LEFT -> {
                if (keyboardReport.leftControl xor keyDown) {
                    changed = true
                }
                keyboardReport.leftControl = keyDown
            }
            KeyEvent.KEYCODE_ALT_LEFT -> {
                if (keyboardReport.leftAlt xor keyDown) {
                    changed = true
                }
                keyboardReport.leftAlt = keyDown
            }
            KeyEvent.KEYCODE_META_LEFT -> {
                if (keyboardReport.leftGui xor keyDown) {
                    changed = true
                }
                keyboardReport.leftGui = keyDown
            }
            KeyEvent.KEYCODE_SHIFT_LEFT -> {
                if (keyboardReport.leftShift xor keyDown) {
                    changed = true
                }
                keyboardReport.leftShift = keyDown
            }
            KeyEvent.KEYCODE_CTRL_RIGHT -> {
                if (keyboardReport.rightControl xor keyDown) {
                    changed = true
                }
                keyboardReport.rightControl = keyDown
            }
            KeyEvent.KEYCODE_ALT_RIGHT -> {
                if (keyboardReport.rightAlt xor keyDown) {
                    changed = true
                }
                keyboardReport.rightAlt = keyDown
            }
            KeyEvent.KEYCODE_META_RIGHT -> {
                if (keyboardReport.rightGui xor keyDown) {
                    changed = true
                }
                keyboardReport.rightGui = keyDown
            }
            KeyEvent.KEYCODE_SHIFT_RIGHT -> {
                if (keyboardReport.rightShift xor keyDown) {
                    changed = true
                }
                keyboardReport.rightShift = keyDown
            }
            else -> {
                // deal with normal keys
                if (keyDown) {
                    if (keyCode != null) {
                        changed = keyboardReport.addKey(keyCode.toByte())
//                        Log.d("mky", "add key" + keyCode.toByte() + changed)
                    } else {
                        Log.d("mky", "null key:$event")
                    }
                } else {
                    if (keyCode != null) {
                        changed = keyboardReport.delKey(keyCode.toByte())
//                        Log.d("mky", "del key" + keyCode.toByte() + changed)
                    } else {
                        Log.d("mky", "null key:$event")
                    }
                }
            }
        }
        return changed
    }

    fun sendNullKeys() {
        keyboardReport.bytes.fill(0)
        if (!hidDevice.sendReport(host, KeyboardReport.ID, keyboardReport.bytes)) {
            Log.e(TAG, "Report wasn't sent")
        }
    }

    fun keyEventHandler(
        keyEventCode: Int,
        event: KeyEvent,
        modifier_checked_state: Int,
        keyCode: Int
    ): Boolean {
//        lock.lock()
        val changed = setKeys(event)
//            if (event.keyCode == KeyEvent.KEYCODE_AT || event.keyCode == KeyEvent.KEYCODE_POUND || event.keyCode == KeyEvent.KEYCODE_STAR) {
//                keyboardReport.leftShift = true
//            }
//            keyboardReport.key1 = byteKey.toByte()
        if (changed) {
            customSender(modifier_checked_state)
        }
//        lock.unlock()
        return true
    }


    fun sendKeyboard(keyCode: Int, event: KeyEvent, modifier_checked_state: Int): Boolean {


        return keyEventHandler(event.keyCode, event, modifier_checked_state, keyCode)


//        return when (event.keyCode) {
//            KeyEvent.KEYCODE_A -> {
//                if(event.isShiftPressed) keyboardReport.leftShift=true
//                //else keyboardReport.leftShift = false
//                if(event.isAltPressed) keyboardReport.leftAlt=true
//                //else keyboardReport.leftAlt=false
//                if(event.isCtrlPressed) keyboardReport.leftControl=true
//                //else keyboardReport.leftControl=false
//                if(event.isMetaPressed) keyboardReport.leftGui=true
//                //else keyboardReport.leftGui=false
//
//                keyboardReport.key1=KeyboardReport.KEYCODE_A.toByte()
//                customSender(modifier_checked_state)
//
//                true
//            }
//
//            KeyEvent.KEYCODE_B -> {
//                setModifiers(event)
//
//                keyboardReport.key1=KeyboardReport.KEYCODE_B.toByte()
//                customSender(modifier_checked_state)
//
//                true
//            }
//
//            else -> false
//        }

    }


//fun sendTestMouseMove() {
//    mouseReport.dxLsb = 20
//    mouseReport.dyLsb = 20
//    mouseReport.dxMsb = 20
//    mouseReport.dyMsb = 20
//    sendMouse()
//}
//
//fun sendTestClick() {
//    mouseReport.leftButton = true
//    sendMouse()
//    mouseReport.leftButton = false
//    sendMouse()
////        Timer().schedule(20L) {
////
////        }
//}
//fun sendDoubleTapClick() {
//    mouseReport.leftButton = true
//    sendMouse()
//    Timer().schedule(100L) {
//        mouseReport.leftButton = false
//        sendMouse()
//        Timer().schedule(100L) {
//            mouseReport.leftButton = true
//            sendMouse()
//            Timer().schedule(100L) {
//                mouseReport.leftButton = false
//                sendMouse()
//            }
//
//
//
//
//        }
//    }
//}
//
//
//
//fun sendLeftClickOn() {
//    mouseReport.leftButton = true
//    sendMouse()
//
//
//}
//fun sendLeftClickOff() {
//    mouseReport.leftButton = false
//    sendMouse()
//
//}
//fun sendRightClick() {
//    mouseReport.rightButton = true
//    sendMouse()
//    Timer().schedule(50L) {
//        mouseReport.rightButton= false
//        sendMouse()
//    }
//}
//
//fun sendScroll(vscroll:Int,hscroll:Int){
//
//    var hscrollmutable=0
//    var vscrollmutable =0
//
//    hscrollmutable=hscroll
//    vscrollmutable= vscroll
//
////        var dhscroll= hscrollmutable-previoushscroll
////        var dvscroll= vscrollmutable-previousvscroll
////
////        dhscroll = Math.abs(dhscroll)
////        dvscroll = Math.abs(dvscroll)
////        if(dvscroll>=dhscroll)
////        {
////            hscrollmutable=0
////
////        }
////        else
////        {
////            vscrollmutable=0
////        }
//    var vs:Int =(vscrollmutable)
//    var hs:Int =(hscrollmutable)
//    Log.i("vscroll ",vscroll.toString())
//    Log.i("vs ",vs.toString())
//    Log.i("hscroll ",hscroll.toString())
//    Log.i("hs ",hs.toString())
//
//
//    mouseReport.vScroll=vs.toByte()
//    mouseReport.hScroll= hs.toByte()
//
//    sendMouse()
//
////        previousvscroll=-1*vscroll
////        previoushscroll=hscroll
//
//
//}


    companion object {
        const val TAG = "KeyboardSender"
    }

}