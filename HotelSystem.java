import java.util.*;
import java.io.*;

class Room {
    int roomId;
    String category;
    boolean isBooked;

    Room(int roomId, String category) {
        this.roomId = roomId;
        this.category = category;
        this.isBooked = false;
    }
}

class Booking {
    int roomId;
    String customerName;
    String category;

    Booking(int roomId, String customerName, String category) {
        this.roomId = roomId;
        this.customerName = customerName;
        this.category = category;
    }
}

public class HotelSystem {

    static ArrayList<Room> rooms = new ArrayList<>();
    static ArrayList<Booking> bookings = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        rooms.add(new Room(1, "Standard"));
        rooms.add(new Room(2, "Standard"));
        rooms.add(new Room(3, "Deluxe"));
        rooms.add(new Room(4, "Deluxe"));
        rooms.add(new Room(5, "Suite"));

        loadBookings(); 

        int choice;

        do {
            System.out.println("\n--- HOTEL MENU ---");
            System.out.println("1. View Available Rooms");
            System.out.println("2. Book Room");
            System.out.println("3. Cancel Booking");
            System.out.println("4. View Bookings");
            System.out.println("5. Exit");

            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    viewRooms();
                    break;

                case 2:
                    bookRoom(sc);
                    break;

                case 3:
                    cancelBooking(sc);
                    break;

                case 4:
                    viewBookings();
                    break;

                case 5:
                    saveBookings();
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice");
            }

        } while (choice != 5);

        sc.close();
    }

    static void viewRooms() {
        System.out.println("\nAvailable Rooms:");
        for (Room r : rooms) {
            if (!r.isBooked) {
                System.out.println("Room " + r.roomId + " - " + r.category);
            }
        }
    }

    static void bookRoom(Scanner sc) {
        System.out.print("Enter Room ID to book: ");
        int id = sc.nextInt();
        sc.nextLine(); // clear buffer

        for (Room r : rooms) {
            if (r.roomId == id && !r.isBooked) {

                System.out.print("Enter your name: ");
                String name = sc.nextLine();

                System.out.print("Enter payment amount: ");
                int amount = sc.nextInt();

                if (amount > 0) {
                    r.isBooked = true;
                    bookings.add(new Booking(id, name, r.category));
                    System.out.println("Booking successful!");
                } else {
                    System.out.println("Payment failed!");
                }

                return;
            }
        }

        System.out.println("Room not available!");
    }

    static void cancelBooking(Scanner sc) {
        System.out.print("Enter Room ID to cancel: ");
        int id = sc.nextInt();

        for (Room r : rooms) {
            if (r.roomId == id && r.isBooked) {
                r.isBooked = false;

                for (int i = 0; i < bookings.size(); i++) {
                    if (bookings.get(i).roomId == id) {
                        bookings.remove(i);
                        break;
                    }
                }

                System.out.println("Booking cancelled.");
                return;
            }
        }

        System.out.println("No booking found.");
    }

    static void viewBookings() {
        System.out.println("\nBooking Details:");
        for (Booking b : bookings) {
            System.out.println("Room " + b.roomId + " | " + b.customerName + " | " + b.category);
        }
    }

    static void saveBookings() throws Exception {
        FileWriter fw = new FileWriter("bookings.txt");

        for (Booking b : bookings) {
            fw.write(b.roomId + "," + b.customerName + "," + b.category + "\n");
        }

        fw.close();
    }

    static void loadBookings() throws Exception {
        File file = new File("bookings.txt");

        if (!file.exists()) return;

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;

        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");

            int id = Integer.parseInt(data[0]);
            String name = data[1];
            String category = data[2];

            bookings.add(new Booking(id, name, category));

            for (Room r : rooms) {
                if (r.roomId == id) {
                    r.isBooked = true;
                }
            }
        }

        br.close();
    }
}