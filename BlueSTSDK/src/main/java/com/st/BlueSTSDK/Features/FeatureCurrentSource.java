/*******************************************************************************
 * COPYRIGHT(c) 2023 EmOne
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *   1. Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *   2. Redistributions in binary form must reproduce the above copyright notice,
 *      this list of conditions and the following disclaimer in the documentation
 *      and/or other materials provided with the distribution.
 *   3. Neither the name of STMicroelectronics nor the names of its contributors
 *      may be used to endorse or promote products derived from this software
 *      without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 ******************************************************************************/
package com.st.BlueSTSDK.Features;

import androidx.annotation.NonNull;

import com.st.BlueSTSDK.Feature;
import com.st.BlueSTSDK.Node;

public class FeatureCurrentSource extends Feature {
    public static final String FEATURE_NAME = "Current Source";
    /**
     * data units
     */
    public static final String FEATURE_UNIT = "mA";
    /**
     * name of the data
     */
    public static final String FEATURE_DATA_NAME = "milli-Ampere";
    /**
     * max acceleration handle by the sensor
     */
    public static final float DATA_MAX = 4095;
    /**
     * min acceleration handle by the sensor
     */
    public static final float DATA_MIN = 0;

    private static final byte[] CURRENT_PERCENT_COMMAND = { 0x02, 0x00 };
    private static final byte[] CURRENT_RAMP_COMMAND  = { 0x01, 0x00 };
    private static final byte[] CURRENT_STEP_COMMAND = { 0x00, 0x00 };

    protected static final Field CURRENT_SOURCE_FILED =
            new Field(FEATURE_DATA_NAME, FEATURE_UNIT, Field.Type.UInt16, DATA_MAX, DATA_MIN);


    /**
     * build a new disabled feature, that doesn't need to be initialized in the node side
     *
     * @param n        node that will update this feature
     */
    public FeatureCurrentSource(Node n) {
        super(FEATURE_NAME, n, new Field[]{CURRENT_SOURCE_FILED});
    }

    protected  FeatureCurrentSource(String name, Node n, Field data[]) {
        super(name,n,data);
        if (data[0]!= CURRENT_SOURCE_FILED) {
            throw new IllegalArgumentException("First data[0] must be FeatureCurrentSource" +
                    ".CURRENT_SOURCE_FILED");
        }//if
    }

    /**
     * extract the current source status, each bit is a current source status
     * @param sample sensor raw data
     * @return current source status data, or 0 if the sample is not valid
     */
    public static byte getCurrentSourceStatus(Sample sample) {
        if(sample!=null)
            if(sample.data.length>0)
                if (sample.data[0] != null)
                    return sample.data[0].byteValue();
        //else
        return 0;
    }

    private static final byte[] EMPTY_COMMAND_DATA = new byte[2];

    /**
     * change the led status to the new one
     * @param newStatus new led status
     * @return true if the command is correctly send
     */
    public boolean changeCurrentSourceStatus(byte newStatus){
        return sendCommand(newStatus,EMPTY_COMMAND_DATA);
    }

    @Override
    protected ExtractResult extractData(long timestamp, @NonNull byte[] data, int dataOffset) {
        return new ExtractResult(new Sample(timestamp,new Number[0],getFieldsDesc()),0);
    }

    /**
     *
     * @param device device where CurrentSourcePercent
     */
    public void CurrentSourcePercent(byte device){
        CURRENT_PERCENT_COMMAND[1] = 0;
        sendCommand(device, CURRENT_PERCENT_COMMAND);
//        writeData(new byte[]{device.getId(),CURRENT_PERCENT_COMMAND[0], CURRENT_PERCENT_COMMAND[1]});
    }

    /**
     *
     * @param device device where CurrentSourceRamp
     */
    public void CurrentSourceRamp(byte device){
        sendCommand(device, CURRENT_RAMP_COMMAND);
//        writeData(new byte[]{device.getId(),CURRENT_RAMP_COMMAND});
    }

    /**
     *
     * @param device device where CurrentSourceStep
     */
    public void CurrentSourceStep(byte device){
        sendCommand(device, CURRENT_STEP_COMMAND);
//        writeData(new byte[]{device.getId(),CURRENT_STEP_COMMAND});
    }
}
