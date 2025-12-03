package tn.hitech.Models;

public class Date {
    private int a, m, j;

    public Date(int j, int m, int a) {
        setA(a);
        setM(m);
        setJ(j);
    }

    public Date(String date) {
        if (date != null && date.contains("-")) {
            String[] parts = date.split("-");

            if (parts.length == 3) {
                try {
                    int j = Integer.parseInt(parts[0].trim());
                    int m = Integer.parseInt(parts[1].trim());
                    int a = Integer.parseInt(parts[2].trim());

                    setA(a);
                    setM(m);
                    setJ(j);

                } catch (NumberFormatException e) {
                    return;
                }
            }
        }
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        if (a > 0) {
            this.a = a;
        }
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        if (m >= 1 && m <= 12) {
            this.m = m;
        }
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        if (j < 1 || m == 0 || a == 0) {
            return;
        }

        int maxDays;

        switch (m) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                maxDays = 31;
                break;

            case 4:
            case 6:
            case 9:
            case 11:
                maxDays = 30;
                break;

            case 2:
                maxDays = bisextiles(a) ? 29 : 28;
                break;

            default:
                return;
        }

        if (j <= maxDays) {
            this.j = j;
        }
    }

    private boolean bisextiles(int year) {
        return (year % 4 == 0);
    }

    @Override
    public String toString() {
        return j + "-" + m + "-" + a;
    }
}
