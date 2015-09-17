package dkf.utility.dataType;

import java.util.HashMap;
import java.util.Map;

import dkf.coder.Coder;
import dkf.coder.HLAasciiCharCoder;
import dkf.coder.HLAasciiStringCoder;
import dkf.coder.HLAbooleanCoder;
import dkf.coder.HLAbyteCoder;
import dkf.coder.HLAfloat32BECoder;
import dkf.coder.HLAfloat32LECoder;
import dkf.coder.HLAfloat64BECoder;
import dkf.coder.HLAfloat64LECoder;
import dkf.coder.HLAinteger16BECoder;
import dkf.coder.HLAinteger16LECoder;
import dkf.coder.HLAinteger32BECoder;
import dkf.coder.HLAinteger32LECoder;
import dkf.coder.HLAinteger64BECoder;
import dkf.coder.HLAinteger64LECoder;
import dkf.coder.HLAunicodeCharCoder;
import dkf.coder.HLAunicodeStringCoder;

@SuppressWarnings("rawtypes")
public final class HLADataTypeUtil {

	private static final Map<String, Class<? extends Coder>> coderMap;

	private static final Map<String, Class> javaTypeMap;

	static {
		coderMap = new HashMap<String, Class<? extends Coder>>();

		coderMap.put("HLAasciiChar", HLAasciiCharCoder.class);
		coderMap.put("HLAasciiString", HLAasciiStringCoder.class);
		coderMap.put("HLAboolean", HLAbooleanCoder.class);

		coderMap.put("HLAbyte", HLAbyteCoder.class);

		coderMap.put("HLAfloat32BE", HLAfloat32BECoder.class);
		coderMap.put("HLAfloat32LE", HLAfloat32LECoder.class);
		coderMap.put("HLAfloat64BE", HLAfloat64BECoder.class);
		coderMap.put("HLAfloat64LE", HLAfloat64LECoder.class);

		coderMap.put("HLAinteger16BE", HLAinteger16BECoder.class);
		coderMap.put("HLAinteger16LE", HLAinteger16LECoder.class);
		coderMap.put("HLAinteger32BE", HLAinteger32BECoder.class);
		coderMap.put("HLAinteger32LE", HLAinteger32LECoder.class);
		coderMap.put("HLAinteger64BE", HLAinteger64BECoder.class);
		coderMap.put("HLAinteger64LE", HLAinteger64LECoder.class);

		coderMap.put("HLAunicodeChar", HLAunicodeCharCoder.class);

		coderMap.put("HLAunicodeString", HLAunicodeStringCoder.class);

		//******************************************************************

		javaTypeMap = new HashMap<String, Class>();

		javaTypeMap.put("HLAasciiChar", Byte.class);
		javaTypeMap.put("HLAasciiString", String.class);
		javaTypeMap.put("HLAboolean", Boolean.class);

		javaTypeMap.put("HLAbyte", Byte.class);

		javaTypeMap.put("HLAfloat32BE", Float.class);
		javaTypeMap.put("HLAfloat32LE", Float.class);
		javaTypeMap.put("HLAfloat64BE", Double.class);
		javaTypeMap.put("HLAfloat64LE", Double.class);

		javaTypeMap.put("HLAinteger16BE", Short.class);
		javaTypeMap.put("HLAinteger16LE", Short.class);
		javaTypeMap.put("HLAinteger32BE", Integer.class);
		javaTypeMap.put("HLAinteger32LE", Integer.class);
		javaTypeMap.put("HLAinteger64BE", Long.class);
		javaTypeMap.put("HLAinteger64LE", Long.class);

		javaTypeMap.put("HLAunicodeChar", Short.class);

		javaTypeMap.put("HLAunicodeString", String.class);

	}



	public static Class<? extends Coder> getClassCoder(String value) {
		return coderMap.get(value);
	}

	public static Class<?> getJavaType(String dataType) {
		return javaTypeMap.get(dataType);
	}


}
