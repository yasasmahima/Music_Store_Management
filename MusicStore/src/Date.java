public class Date {
    //Declaring variables
    private int day;
    private int month;
    private int year;


    //Adding the default constructor
    public Date() {
    }

    //Creating a parameterized constructor to initialized the variables
    public Date(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;

    }

    @Override   //overriding to Sring method
    public String toString() {
        return ""+ day +
                "-" + month +
                "-" + year ;

    }

    //Adding getter to retrieve the values of the variables
    public int getDay() {
        return day;
    }       //get day

    public int getMonth() {
        return month;
    }  //get month

    public int getYear() {
        return year;
    }    //get year


    //set the day
    public void setDay(int day) {
        if ((day >= 1) && (day <= 31)) {
            this.day = day;             //Validate day if true set else throws an exception
        } else {
            throw new IllegalArgumentException("Invalid Day!");
        }

    }

    //set the month
    public void setMonth(int month) {
        if ((month >= 1) && (month <= 12)) {
            this.month = month;               //validate month if true set else throws an exception
        } else {
            throw new IllegalArgumentException("Invalid Month!");
        }
    }

    //set the year
    public void setYear(int year) {
        if ((year >= 1900) && (year <= 2050)) {
            this.year = year;                     //Validate year if true set else throws an exception
        } else {
            throw new IllegalArgumentException("Invalid Year!");
        }
    }
}
