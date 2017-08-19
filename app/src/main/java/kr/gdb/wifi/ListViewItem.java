package kr.gdb.wifi;

/**
 * Created by junwon on 2017-08-17.
 */

public class ListViewItem {
    private String name, tel, addr;
    private double longt, lat;

    public ListViewItem(String name, String tel, String addr, double longt, double lat) {
        this.name = name;
        this.tel = tel;
        this.addr = addr;
        this.longt = longt;
        this.lat = lat;
    }

    public String getName() {
        return name;
    }

    public String getTel() {
        return tel;
    }

    public String getAddr() {
        return addr;
    }

    public double getLongt() {
        return longt;
    }

    public double getLat() {
        return lat;
    }
}
