package temperatureConverter;

import java.util.*;

public class TemperatureConverter {
    public static double celsiusToFahrenheit(double celsius) {
        return (celsius * 9/5) + 32;
    }

    public static double fahrenheitToCelsius(double fahrenheit) {
        return (fahrenheit - 32) * 5/9;
    }

    public static double celsiusToKelvin(double celsius) {
        return celsius + 273.15;
    }

    public static double kelvinToCelsius(double kelvin) {
        return kelvin - 273.15;
    }

    public static void displayMenu() {
        System.out.println("\n=== Temperature Converter ===");
        System.out.println("1. Celsius to Fahrenheit");
        System.out.println("2. Fahrenheit to Celsius");
        System.out.println("3. Celsius to Kelvin");
        System.out.println("4. Kelvin to Celsius");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }

    public static String formatTemp(double temp) {
        return String.format("%.2f", temp);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while(running) {
            displayMenu();
            int choice = sc.nextInt();

            switch(choice) {
                case 1:
                    System.out.print("Enter temperature in Celsius: ");
                    double celsius = sc.nextDouble();
                    double fahrenheit = celsiusToFahrenheit(celsius);
                    System.out.println(formatTemp(celsius) + "°C = " + formatTemp(fahrenheit) + "°F");
                    break;

                case 2:
                    System.out.print("Enter temperature in Fahrenheit: ");
                    double fahr = sc.nextDouble();
                    double celsi = fahrenheitToCelsius(fahr);
                    System.out.println(formatTemp(fahr) + "°F = " + formatTemp(celsi) + "°C");
                    break;

                case 3:
                    System.out.print("Enter temperature in Celsius: ");
                    double cel = sc.nextDouble();
                    double kelvin = celsiusToKelvin(cel);
                    System.out.println(formatTemp(cel) + "°C = " + formatTemp(kelvin) + "K");
                    break;

                case 4:
                    System.out.print("Enter temperature in Kelvin: ");
                    double kel = sc.nextDouble();
                    double celsiusFromKelvin = kelvinToCelsius(kel);
                    System.out.println(formatTemp(kel) + "K = " + formatTemp(celsiusFromKelvin) + "°C");
                    break;

                case 5:
                    System.out.println("Thank you for using Temperature Converter!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice! Please enter a number between 1-5.");
            }
        }
    }
}