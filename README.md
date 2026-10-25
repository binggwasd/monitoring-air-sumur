# Sistem Monitoring Kondisi dan Temperatur Air Sumur Berbasis IoT Nirkabel

Sistem ini didesain untuk memantau kondisi kejernihan dan suhu air sumur, kemudian informasi tersebut akan ditampilkan pada aplikasi mobile berbasis cloud secara langsung. Walaupun desain dari sistem ini diperuntukkan untuk pemantauan air sumur, sistem ini tetap dapat dipakai di perairan mana pun dengan catatan gateway tetap terhubung ke internet.

Hardware dari sistem ini dibagi menjadi 2 bagian, sensor node dan gateway. Sensor node akan mengirimkan data melalui kabel ke gateway, kemudian gateway akan mengirimkan data tersebut ke cloud melalui internet (Wi-Fi) yang akan ditampilkan pada aplikasi mobile. Sistem ini dibagi menjadi dua agar sensor node dapat diletakkan di mana saja dengan catatan kabel cukup panjang. 

Sensor node tidak memakai baterai karena bersifat aktif setiap saat. Hal tersebut akan menguras baterai dengan sangat cepat, oleh karena itu daya akan disalurkan melalui kabel yang sama untuk mengirim data dari Gateway menggunakan passive Power over Ethernet (PoE).

Data dari sensor akan dapat dilihat melalui aplikasi mobile yang menggunakan cloud service Firebase sebagai backend serverless. Data akan disimpan di Cloud Firestore dari Firebase.

## Arsitektur Hardware
Arsitektur dari node sensor:
<img width="1742" height="342" alt="Sensor Node V1 DM" src="https://github.com/user-attachments/assets/8d3563f7-a07a-44e4-be3a-7bc6e8fd00c1" />

Arsitektur dari gateway:
<img width="1320" height="622" alt="Gateway V1 DM" src="https://github.com/user-attachments/assets/298108c5-a536-4638-9652-84335fd91eb6" />

## Log Pengembangan
Log pengembangan dapat diakses melalui link berikut:<br>
https://docs.google.com/spreadsheets/d/1IfR_lE6kkHo0-PhQHJhc2RqZ3cYVjlXsxDCJ8bkbZJM/edit?gid=2011546994#gid=2011546994
