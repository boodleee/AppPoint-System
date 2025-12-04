import java.time.LocalDate;
import java.util.*;


public class AppPoint {

    // It represents the appointment entry of ID, name, email and date
    static class Appointment {
        private final int id;
        private String name;
        private String email;
        private LocalDate date;

        // the one that construct the creation of new appointments
        public Appointment(int id, String name, String email, LocalDate date) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.date = date;
        }

        // Getter methods it is used to access private fields
        public int getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public LocalDate getDate() { return date; }

        // Setter methods it is used to update appointment details  
        public void setName(String name) { this.name = name; }
        public void setEmail(String email) { this.email = email; }
        public void setDate(LocalDate date) { this.date = date; }

        // This converts the appointments info into text on output
        @Override
        public String toString() {
            return "[ID:" + id + "] " + name + " → " + date + " (" + email + ")";
        }
    }

    // APPOINTMENT MANAGER
    //it handles the appointment features such as booking, canceling, limits, etc.
    static class AppointmentManager {
        private final List<Appointment> appointments = new ArrayList<>();
        private final Map<LocalDate, Integer> limits = new HashMap<>();
        private int nextId = 1;

        // ENCAPSULATION: callers never touch the real list, only this snapshot
        public List<Appointment> getAppointmentsSnapshot() {
            return new ArrayList<>(appointments);
        }

        // ABSTRACTION + OVERLOADING (polymorphism at compile time)
        // High-level booking method that takes separate values
        public boolean tryBook(String name, LocalDate date, String email) {
            if (!limits.containsKey(date)) return false;
            long count = appointments.stream().filter(a -> a.getDate().equals(date)).count();
            if (count >= limits.get(date)) return false;
            appointments.add(new Appointment(nextId++, name, email, date));
            return true;
        }

        // Overloaded version that works directly with an Appointment object
        public boolean tryBook(Appointment a) {
            return tryBook(a.getName(), a.getDate(), a.getEmail());
        }

        // Book with auto shift (returns booked Appointment or null)
        public Appointment bookWithAutoShift(String name, LocalDate date, String email, Scanner sc) {
            while (true) {
                if (!limits.containsKey(date)) {
                    // Admin didn't set limit
                    return null;
                }
                if (tryBook(name, date, email)) {
                    return appointments.get(appointments.size() - 1);
                }
                // date exists but full
                System.out.println("\n " + date + " is FULLY BOOKED.");
                System.out.print("Try next day (" + date.plusDays(1) + ")? (Y/N): ");
                if (!sc.nextLine().trim().equalsIgnoreCase("Y")) {
                    return null;
                }
                date = date.plusDays(1);
            }
        }

        // Set limit range 
        public void setLimitRange(String input, int limit) {
            String[] parts = input.split(",");
            for (String p : parts) {
                p = p.trim();
                if (p.isEmpty()) continue;
                if (p.contains(":")) {
                    String[] r = p.split(":");
                    try {
                        LocalDate start = LocalDate.parse(r[0].trim());
                        LocalDate end = LocalDate.parse(r[1].trim());
                        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1))
                            limits.put(d, limit);
                    } catch (Exception ex) { System.out.println("Invalid range: " + p); }
                } else {
                    try { limits.put(LocalDate.parse(p), limit); }
                    catch (Exception ex) { System.out.println("Invalid date: " + p); }
                }
            }
        }

        // Get a copy of appointments list for a date
        public List<Appointment> getAppointmentsForDate(LocalDate date) {
            List<Appointment> result = new ArrayList<>();
            for (Appointment a : appointments) if (a.getDate().equals(date)) result.add(a);
            return result;
        }

        // View all limits (dates that have limits)
        public Set<LocalDate> getAllLimitDates() {
            return new HashSet<>(limits.keySet());
        }

        // it stores the maximum number of appointments allowed on each date.
        public int getLimit(LocalDate d) {
            return limits.getOrDefault(d, -1);
        }

        // Get remaining slots for a date
        public int getRemaining(LocalDate d) {
            if (!limits.containsKey(d)) return Integer.MIN_VALUE;
            int limit = limits.get(d);
            int booked = (int) appointments.stream().filter(a -> a.getDate().equals(d)).count();
            return limit - booked;
        }

        // View available dates and remaining slots
        public void printAvailable() {
            System.out.println("\n========= AVAILABLE SCHEDULES =========");
            if (limits.isEmpty()) { System.out.println("No schedules set.\n"); return; }

            TreeSet<LocalDate> sorted = new TreeSet<>(limits.keySet());
            for (LocalDate d : sorted) {
                long booked = appointments.stream().filter(a -> a.getDate().equals(d)).count();
                int rem = limits.get(d) - (int) booked;
                System.out.printf("%-12s — %d slot(s) available%n", d, rem);
            }
            System.out.println("=======================================\n");
        }

        // View all appointments 
        public void printAllAppointments() {
            if (limits.isEmpty()) { System.out.println("\n No scheduled dates available.\n"); return; }

            System.out.println("\n====================== APPOINTMENTS ======================");
            System.out.printf("%-12s | %-18s | %-25s | %-16s%n", "DATE", "NAME", "EMAIL", "STATUS");
            System.out.println("------------------------------------------------------------------");

            TreeSet<LocalDate> sorted = new TreeSet<>(limits.keySet());
            for (LocalDate d : sorted) {
                int limit = limits.get(d);
                List<Appointment> list = getAppointmentsForDate(d);
                int remaining = limit - list.size();
                String status = remaining <= 0 ? "FULL" : remaining + " slots left";

                if (list.isEmpty()) {
                    System.out.printf("%-12s | %-18s | %-25s | %-16s%n", d, "-", "-", status);
                } else {
                    boolean first = true;
                    for (Appointment a : list) {
                        System.out.printf("%-12s | %-18s | %-25s | %-16s%n",
                                first ? d.toString() : "", a.getName(), a.getEmail(), first ? status : "");
                        first = false;
                    }
                }
            }
            System.out.println("------------------------------------------------------------------");
        }

        // It removes and appointment using the id
        public boolean cancelById(int id) {
            Iterator<Appointment> it = appointments.iterator();
            while (it.hasNext()) {
                if (it.next().getId() == id) { it.remove(); return true; }
            }
            return false;
        }

        // cancels the appointment using using id and email 
        public boolean cancelUserAppointment(int id, String email) {
            Iterator<Appointment> it = appointments.iterator();
            while (it.hasNext()) {
                Appointment a = it.next();
                if (a.getId() == id && a.getEmail().equalsIgnoreCase(email)) {
                    it.remove();
                    return true;
                }
            }
            return false;
        }

        // Find user appointment by id + email (for modification)
        public Appointment findUserAppointment(int id, String email) {
            for (Appointment a : appointments) {
                if (a.getId() == id && a.getEmail().equalsIgnoreCase(email)) return a;
            }
            return null;
        }

        // Check if a date has capacity for moving an appointment (exclude that appointment from count)
        public boolean canMoveToDate(LocalDate newDate, int excludeId) {
            if (!limits.containsKey(newDate)) return false;
            long count = appointments.stream()
                    .filter(a -> a.getDate().equals(newDate) && a.getId() != excludeId)
                    .count();
            return count < limits.get(newDate);
        }
    }

    // Abstract Panel 
    // INHERITANCE & POLYMORPHISM: AdminPanel and UserPanel both extend Panel
    static abstract class Panel {
        protected final AppointmentManager manager;
        protected final Scanner scanner;

        protected Panel(AppointmentManager manager, Scanner scanner) {
            this.manager = manager;
            this.scanner = scanner;
        }

        // Small shared helper to show inheritance of behavior
        protected String readLine(String prompt) {
            System.out.print(prompt);
            return scanner.nextLine().trim();
        }

        // Each panel implements its own menu loop (runtime polymorphism)
        public abstract void show();
    }

    //  AdminPanel = it lets the admin manage limits, view all appointment and delete any appointment using ID and i will keep looping until the admin choose logout
    static class AdminPanel extends Panel {
        public AdminPanel(AppointmentManager manager, Scanner scanner) {
            super(manager, scanner);
        }
// it prints the Admin menu options
        @Override
        public void show() {
            boolean exitSession = false;
            while (!exitSession) {
                System.out.println("\n========== MENU ==========");
                System.out.println("1) Scheduling Limits");
                System.out.println("2) View All Appointments");
                System.out.println("3) Terminate Appointment");
                System.out.println("0) Logout");
                String ch = readLine("> ");

                switch (ch) {
                    case "1" -> manageLimits();
                    case "2" -> manager.printAllAppointments();
                    case "3" -> {
                        try {
                            int id = Integer.parseInt(readLine("Appointment ID: "));
                            System.out.println(manager.cancelById(id) ? " Removed." : "✘ ID not found.");
                        } catch (Exception e) { System.out.println("Invalid ID."); }
                    }
                    case "0" -> exitSession = true;
                    default -> System.out.println("Invalid.");
                }
            }
        }
 // it lets the admin input the date and maximumum number of bookings per day
        private void manageLimits() {
            while (true) {
                System.out.println("\n--- LIMIT SETTINGS ---");
                System.out.println("1) Set Limit");
                System.out.println("2) Edit Limit");
                System.out.println("0) Back");
                String x = readLine("> ");

                if (x.equals("0")) break;

                System.out.println("\nAccepted Date Formats:");
                System.out.println("Single: YYYY-MM-DD");
                System.out.println("Range: YYYY-MM-DD:YYYY-MM-DD");
                System.out.println("Multiple: YYYY-MM-DD, YYYY-MM-DD");

                String input = readLine("\nEnter date/s: ");
                String limitStr = readLine("Limit value: ");
                try {
                    int limit = Integer.parseInt(limitStr);
                    manager.setLimitRange(input, limit);
                    System.out.println(x.equals("1") ? " Limit created." : " Limit updated.");
                } catch (Exception e) {
                    System.out.println("Invalid limit.");
                }
            }
        }
    }

    // UserPanel 
    // this is the user’s home screen where they can book, cancel, or edit their appointment, and it keeps looping until they choose Logout.
    static class UserPanel extends Panel {
        public UserPanel(AppointmentManager manager, Scanner scanner) {
            super(manager, scanner);
        }

        @Override
        public void show() {
            boolean exitSession = false;
            while (!exitSession) {
                System.out.println("\n========== MENU ==========");
                System.out.println("1) Book Appointment");
                System.out.println("2) Cancel My Appointment");
                System.out.println("3) Modify My Appointment");
                System.out.println("0) Logout");
                String ch = readLine("> ");

                switch (ch) {
                    case "1" -> bookingMenu();
                    case "2" -> cancelMyAppointment();
                    case "3" -> modifyMyAppointment();
                    case "0" -> exitSession = true;
                    default -> System.out.println("Invalid.");
                }
            }
        }
// this is where the users can see the dates that is are available for booking or to book an appointment. It keeps looping until the user logged out.
        private void bookingMenu() {
            while (true) {
                System.out.println("\n--- BOOKING MENU ---");
                System.out.println("1) Book Now");
                System.out.println("2) View Available Schedules");
                System.out.println("0) Back");
                String u = readLine("> ");

                if (u.equals("2")) manager.printAvailable();
                else if (u.equals("0")) break;
                else if (u.equals("1")) {
                    try {
                        LocalDate date = LocalDate.parse(readLine("Preferred date (YYYY-MM-DD): "));
                        String name = readLine("Name: ");
                        String email = readLine("Email: ");
                        Appointment booked = manager.bookWithAutoShift(name, date, email, scanner);
                        if (booked != null) System.out.println("\n BOOKED → " + booked);
                        else System.out.println("Invalid date or booking cancelled.");
                    } catch (Exception e) { System.out.println("Invalid date."); }
                } else {
                    System.out.println("Invalid.");
                }
            }
        }
// It lets user cancel their own appointment if they can prove it is theirs by giving both the correct ID and email.
        private void cancelMyAppointment() {
            try {
                int id = Integer.parseInt(readLine("Appointment ID: "));
                String email = readLine("Email: ");
                System.out.println(manager.cancelUserAppointment(id, email)
                        ? " Cancelled." : "✘ Failed — ID/Email mismatch");
            } catch (Exception e) { System.out.println("Invalid input."); }
        }
//  This lets a user find their appointment using ID and email, then change their name, email or date, but only if the new date is valid and not fully booked.
        private void modifyMyAppointment() {
            try {
                int id = Integer.parseInt(readLine("Appointment ID: "));
                String email = readLine("Current Email: ");

                Appointment a = manager.findUserAppointment(id, email);
                if (a == null) {
                    System.out.println("✘ Failed — ID/Email mismatch or not found.");
                    return;
                }

                System.out.println("\nCurrent: " + a);

                String newName = readLine("New name (leave blank to keep): ");
                if (!newName.isEmpty()) a.setName(newName);

                String newEmail = readLine("New email (leave blank to keep): ");
                if (!newEmail.isEmpty()) a.setEmail(newEmail);

                String newDateStr = readLine("New date YYYY-MM-DD (leave blank to keep): ");
                if (!newDateStr.isEmpty()) {
                    try {
                        LocalDate newDate = LocalDate.parse(newDateStr);
                        if (!manager.canMoveToDate(newDate, a.getId())) {
                            System.out.println("✘ Cannot move — either no limit or target date is fully booked.");
                            return;
                        }
                        a.setDate(newDate);
                    } catch (Exception e) {
                        System.out.println("Invalid date format.");
                        return;
                    }
                }

                System.out.println("✔ Appointment updated: " + a);
            } catch (Exception e) {
                System.out.println("Invalid input.");
            }
        }
    }

    //  MAIN Program
    // This is the front of the program. This shows the welcome screen and lets the user choose Admin/User/Exit
    public static void main(String[] args) {
        AppointmentManager manager = new AppointmentManager();
        Scanner sc = new Scanner(System.in);

        System.out.println("=====================================");
        System.out.println("      WELCOME TO AppPoint System     ");
        System.out.println("=====================================");
        System.out.print("Proceed? (Y/N): ");
        String proceed = sc.nextLine().trim().toUpperCase();
        if (!proceed.equals("Y")) {
            System.out.println("Program ended.");
            return;
        }

        while (true) {
            System.out.println("\n-------------------------------------");
            System.out.println("Select Account Type");
            System.out.println("[A] Admin");
            System.out.println("[U] User");
            System.out.println("[X] Exit");
            System.out.println("-------------------------------------");

            System.out.print("> ");
            String role = sc.nextLine().trim().toUpperCase();
            if (role.equals("X")) {
           	    System.out.println("Program ended.");
                return;
            }

            // POLYMORPHISM: same variable type, different show() at runtime
            Panel panel;
            if (role.equals("A")) panel = new AdminPanel(manager, sc);
            else panel = new UserPanel(manager, sc);

            panel.show();
        }
    }
}