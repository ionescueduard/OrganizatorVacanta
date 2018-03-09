# OrganizatorVacanta

Cateva detali de implementare.

Am stocat datele in 2 metode:
  1. prima metoda:
    Am creat un hashmap care contine toate obiectele "Tara"
    -fiecare obiect "Tara" contine un string pentru nume si un HashMap pentru obiecte de tip "Judet"
    -fiecare obiect "Judet" contine un string pentur nume si un HashMap pentru obiecte de tip "Oras"
    -fiecare obiect "Oras" contine un string pentur nume si un HashMap pentru obiecte de tip "Locatie"
    In aceasta prima metoda de stocare am folosit HashMap-uri pentru a putea gasi Tara/Judetul/Oras-ul in complexitate O(1) cand caut cele top5 din Tara/Judet/Oras;
    
  2. a doua metoda:
    Am creat un HashMap care contine toate obiectele de tip Locatie, iar ca key, numele acestora.
      -am folosit aceasta a doua metoda de stocare pentru taskul de a gasi o lacatie, dupa nume, in complexitate O(1);
