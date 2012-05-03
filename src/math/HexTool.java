package math;

public class HexTool {

	public static int intFromHexString(String hexString) {
		char[] chars = hexString.toLowerCase().toCharArray();
		
		int intValue = 0;;
		for (int i = 1; i <= chars.length; i++) {
			switch (chars[chars.length-i]) {
			case '0' :
				intValue = intValue | (0x0 << (i-1)*4);
				break;
			case '1' :
				intValue = intValue | (0x1 << (i-1)*4);
				break;
			case '2' :
				intValue = intValue | (0x2 << (i-1)*4);
				break;
			case '3' :
				intValue = intValue | (0x3 << (i-1)*4);
				break;
			case '4' :
				intValue = intValue | (0x4 << (i-1)*4);
				break;
			case '5' :
				intValue = intValue | (0x5 << (i-1)*4);
				break;
			case '6' :
				intValue = intValue | (0x6 << (i-1)*4);
				break;
			case '7' :
				intValue = intValue | (0x7 << (i-1)*4);
				break;
			case '8' :
				intValue = intValue | (0x8 << (i-1)*4);
				break;
			case '9' :
				intValue = intValue | (0x9 << (i-1)*4);
				break;
			case 'a' :
				intValue = intValue | (0xa << (i-1)*4);
				break;
			case 'b' :
				intValue = intValue | (0xb << (i-1)*4);
				break;
			case 'c' :
				intValue = intValue | (0xc << (i-1)*4);
				break;
			case 'd' :
				intValue = intValue | (0xd << (i-1)*4);
				break;
			case 'e' :
				intValue = intValue | (0xe << (i-1)*4);
				break;
			case 'f' :
				intValue = intValue | (0xf << (i-1)*4);
				break;
			}
		}
		return intValue;
	}
	
}
