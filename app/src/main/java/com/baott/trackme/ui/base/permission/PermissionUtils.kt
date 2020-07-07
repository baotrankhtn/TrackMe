/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 The Brown Arrow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.blab.moviestv.ui.base.permission

import android.Manifest

object PermissionUtils {

    val Manifest_GROUP_CAMERA = Manifest.permission_group.CAMERA
    val Manifest_GROUP_CONTACTS = Manifest.permission_group.CONTACTS
    val Manifest_GROUP_CALENDAR = Manifest.permission_group.CALENDAR
    val Manifest_GROUP_SMS = Manifest.permission_group.SMS
    val Manifest_GROUP_MICROPHONE = Manifest.permission_group.MICROPHONE
    val Manifest_GROUP_PHONE = Manifest.permission_group.PHONE
    val Manifest_GROUP_SENSORS = Manifest.permission_group.SENSORS

    val Manifest_GROUP_LOCATION = Manifest.permission_group.LOCATION
    val Manifest_GROUP_STORAGE = Manifest.permission_group.STORAGE

    val Manifest_CAMERA = Manifest.permission.CAMERA
    val Manifest_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
    val Manifest_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE

    val Manifest_READ_CALENDAR = Manifest.permission.READ_CALENDAR
    val Manifest_WRITE_CALENDAR = Manifest.permission.WRITE_CALENDAR

    val Manifest_READ_CONTACTS = Manifest.permission.READ_CONTACTS
    val Manifest_WRITE_CONTACTS = Manifest.permission.WRITE_CONTACTS

    val Manifest_GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS
    val Manifest_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    val Manifest_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION

    val Manifest_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE
    val Manifest_CALL_PHONE = Manifest.permission.CALL_PHONE
    val Manifest_READ_CALL_LOG = Manifest.permission.READ_CALL_LOG
    val Manifest_WRITE_CALL_LOG = Manifest.permission.WRITE_CALL_LOG
    val Manifest_ADD_VOICEMAIL = Manifest.permission.ADD_VOICEMAIL
    val Manifest_USE_SIP = Manifest.permission.USE_SIP
    val Manifest_PROCESS_OUTGOING_CALLS = Manifest.permission.PROCESS_OUTGOING_CALLS

    val Manifest_SEND_SMS = Manifest.permission.SEND_SMS
    val Manifest_RECEIVE_SMS = Manifest.permission.RECEIVE_SMS
    val Manifest_READ_SMS = Manifest.permission.READ_SMS
    val Manifest_RECEIVE_WAP_PUSH = Manifest.permission.RECEIVE_WAP_PUSH
    val Manifest_RECEIVE_MMS = Manifest.permission.RECEIVE_MMS

    val Manifest_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO

    val Manifest_BODY_SENSORS = Manifest.permission.BODY_SENSORS
}