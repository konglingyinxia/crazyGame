package com.crazy.util.twm;

public class Test {

	public static void main(String[] args) throws Exception {

		// 生成带logo 的二维码
		String text = "http://www.baidu.com?a=456454";
//		QRCodeUtil.encode(text, "", "d://", true);
		QRCodeUtil.encode(text, "7f32f66281d84231a025a498631ad730", "d://", true);
//		QRCodeUtil.encode(text, "21313213131311", "d://", true);

		// 生成不带logo 的二维码
		/*String textt = "http://www.baidu.com";
		QRCodeUtil.encode(textt, "", "d://", true);
*/
		// 指定二维码图片，解析返回数据
//		System.out.println(QRCodeUtil.decode("d://75040887.jpg"));

	}
}
