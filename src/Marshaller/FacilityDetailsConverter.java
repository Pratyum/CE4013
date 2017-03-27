/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Marshaller;

/**
 *
 * @author pratyumjagannath
 */
public class FacilityDetailsConverter implements Convertor {

    @Override
    public int getByteCount() {
        return Long.BYTES + Float.BYTES + Integer.BYTES;
    }

    @Override
    public Object fromBytes(byte[] bytes) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public byte[] toBytes(Object data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
