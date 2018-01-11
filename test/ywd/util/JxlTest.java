package ywd.util;

import jxl.format.BoldStyle;
import jxl.format.Colour;

public class JxlTest {

    public static void main(String[] args) {
        System.out.println(Colour.RED.getDescription() + ":" + Colour.RED.getValue());
        System.out.println(Colour.BLUE.getDescription() + ":" + Colour.BLUE.getValue());
        System.out.println(Colour.GREEN.getDescription() + ":" + Colour.GREEN.getValue());
        System.out.println(Colour.BLUE2.getDescription() + ":" + Colour.BLUE2.getValue());

        System.out.println(BoldStyle.NORMAL.getDescription() + ":" + BoldStyle.NORMAL.getValue());
        System.out.println(BoldStyle.BOLD.getDescription() + ":" + BoldStyle.BOLD.getValue());
    }
}
