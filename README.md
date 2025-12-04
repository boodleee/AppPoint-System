<h1 align="center">I</h1>


<h1 align="center"> - AppPoint - </h1>
<p align="center"> A fully object-oriented appointment scheduling system where admins set daily appointment limits and users can book, modify, or cancel appointments, with automatic date shifting when chosen schedules reach capacity, ensuring organized and validated booking management. </p>

---

<h3 align="right"> GoM Team </h3>

### <p align="right"> Alvarez, Jimkyle Roniel B. </p>

### <p align="right"> Hilario, Luis Miguel R. </p>

### <p align="right"> Ortega, Giovrei McGave V. </p>

#### <p align="right"> developers from IT-2112 </p>

-------------
<br>
<br>
<br>

<h1 align="center">II</h1>

-------------
## - Project Description -

AppPoint is a Java-based appointment scheduling system that allows




**(1) admins to set limits and organize appointment scheduling;**

**(2) users to register appointments by name, email, and date.**



------------------
It demonstrates **Object-Oriented Programming (OOP)** principles:

| OOP Concept   | Part of the Program                          | Meaning                                     |
| ------------- | -------------------------------------------- | ------------------------------------------- |
| Encapsulation | `Appointment` & `AppointmentManager`         | Protects and controls access to data        |
| Abstraction   | Booking process, auto-shift, input handling  | Hides complexity, simplifies usage          |
| Inheritance   | `Panel` parent class                         | Eliminates repetition, organizes UI logic   |
| Polymorphism  | Overridden `show()` + overloaded `tryBook()` | Flexible behavior at runtime & compile time |


The system does not rely on external databases; all data is managed in memory while the program is running.  


-------------
<br>
<br>
<br>


<h1 align="center">III</h1>

-------------
## - Features - 

| **ADMIN**                                               | **USER**                                       |
| ------------------------------------------------------- | -----------------------------------------------|
| <p align="center"> Set scheduling limits            </p>| <p align="center">Book appointment         </p>|
| <p align="center"> Edit date limits                 </p>| <p align="center">View available schedules </p>|
| <p align="center"> View all appointments            </p>| <p align="center">Modify own appointment   </p>|
| <p align="center"> Cancel/terminate any appointment </p>| <p align="center">Cancel own appointment   </p>|
| <p align="center"> Logout                           </p>| <p align="center">Logout                   </p>|


-------------
<br>
<br>
<br>

<h1 align="center">IV</h1>


-------------  
## - Program Structure -

 AppPoint  
└── AppPoint.java   — Entire program contained in a single file

The entire system is coded inside **AppPoint.java** including the Appointment model, booking functions, admin/user menu, and the main program. This format is simple and easier to run since no additional files are needed.  


-------------
<br>
<br>
<br>


<h1 align="center">V</h1>


-----
## - Run the Program -

**Requirements**
- Java JDK 17+
- Terminal / CMD

**Compile**
```bash
javac AppPoint.java
```

**Run**
```bash
java AppPoint
```

**Flow**
```
Welcome → Choose Role
A = Admin
U = User
X = Exit
```

-------------
<br>
<br>
<br>


<h1 align="center">VI</h1>

-------------
## - Output Samples -
----

### I. Welcome Startup Prompt
```bash
====================================
      WELCOME TO APPOINT SYSTEM     
====================================
Proceed? (Y/N):
```

----
### II. Role Selection (A / U / X)
```bash
-------------------------------------
Select Account Type
[A] Admin
[U] User
[X] Exit
-------------------------------------
>
```
----
### III. Admin — Scheduling Limit Setup
```bash
========== MENU ==========
1) Scheduling Limits
2) View All Appointments
3) Terminate Appointment
0) Logout
> 1

--- LIMIT SETTINGS ---
1) Set Limit
2) Edit Limit
0) Back

Accepted Date Formats:
Single:  YYYY-MM-DD
Range:   YYYY-MM-DD:YYYY-MM-DD
Multi:   YYYY-MM-DD, YYYY-MM-DD

Enter date/s: 2025-01-10
Limit value: 5
Limit created.
```
----
### IV. User — Booking an Appointment
```bash
========== MENU ==========
1) Book Appointment
2) Cancel My Appointment
3) Modify My Appointment
0) Logout
> 1

--- BOOKING MENU ---
1) Book Now
2) View Available Schedules
0) Back
> 1

Preferred date (YYYY-MM-DD): 2025-01-10
Name: Juan Dela Cruz
Email: juan@mail.com

BOOKED → [ID:1] Juan Dela Cruz → 2025-01-10 (juan@mail.com)
```


-------------
<br>
<br>
<br>


<h1 align="center">VII</h1>

-------------
## - Acknowledgement -
----

<h3>We express our deepest gratitude to everyone who contributed to the successful development of this The AppPoint System.</h3>

- To the **Almighty Father**, for the constant clarity, upbringing, and steadyness he graced us.  
- To our **instructor**, for the guidance, constructive feedback, and continuous support that shaped the growth of this project.  
- To our **classmates and peers**, for the shared ideas, collaboration, and motivation that helped us improve as we worked.  
- To the **resource authors, online documentation, and technical references**, which allowed us to better understand Java concepts, debug errors, and implement features effectively.  
- And finally, to the **group members** — for the cooperation, dedication, and perseverance that made this system possible.


> *This project stands as the result of collective effort, shared learning, and commitment to quality.*

------------

<h5 align="center">This program is intended for learning and academic purposes, and should be used responsibly and securely.</h5>


