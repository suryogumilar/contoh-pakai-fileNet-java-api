# Java API samples untuk IBM FileNet

Project ini berisi sample contoh penggunaan JAVA API Library untuk dapat berinterkasi dengan API IBM FileNet.


#### Library Setting

Library yang dibutuhkan dalam pengembangan aplikasi adalah sebagai berikut:

 1. com.ibm.stax-api
 2. com.ibm.xml.xlxp.scanner
 3. com.ibm.xml.xlxp.scanner.utils
 4. com.ibm.filenet.jace
 5. com.ibm.ecm.j2ee.connector

kelima library ini yang berbetuk .jar files diperoleh dari dalam instalasi IBM FileNet. Karena contoh ini menggunakan pom maven. maka ke lima library ini dianjurkan diinstal ke dalam lokal maven repo terlebih dahulu karena kelima library ini proprietary dan tidak terdapat di maven repository publik.


#### Maven repo Setting

diasumsikan PC yang digunakan untuk development sudah terlinstal dengan maven. Untuk petunjuk instalasi bisa dilihat di [sini](https://maven.apache.org/install.html)

Install kelima library ke dalam maven repo. Contoh command :

`mvn install:install-file -Dfile=Jace.jar -DgroupId=com.ibm -DartifactId=com.ibm.filenet.jace -Dversion=5.2.1.3 -Dpackaging=jar -DlocalRepositoryPath=C:\Users\ecpa\.m2\repository`

command tersebut akan menginstall ke dalam maven repo yang berada di folder *C:\Users\ecpa\.m2\repository*.

Lakukan command tersebut untuk menginstall kelima jar ke dalam maven repo. Sesuaikan nilai *version* dengan file *pom.xml*


#### Develop Apps

Prosedur umum dalam penggunaan API Filenet ini bisa dibreakdown menjadi:

 1. Memperoleh Connection ke FileNet menggunakan  Factory class
 2. Melakukan autentikasi subject connection menggunakan Connection
 3. Memperoleh object Domain yang digunakan untu mengambil Object Store.
 4. Memperoleh ObjectStore yang digunakan untuk berinteraksi dengan CE (Content Engine)
 4. Logout koneksi setelah selesai 


Variabel-variabel yang umumnya dibutuhkan untuk koneksi dan berinteraksi dengan API Filenet a.l:

 1. Url dari FileNet API
 2. User dan password
 3. JAASStanzaName
 4. Nama Object Store tempat file-file atau object-object yang terismpan dalam FileNet.

Nilai dari variabel tersebut ditentukan oleh admin/PIC FileNet

Selain itu terdapat pula variabel document Class dimana file-file yang ada dalam filenet dikelompokkan berdasarkan kategori.

