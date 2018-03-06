# indianapolis
Fulfilment service project

### Create Module
- Klik kanan parent module, New > Module
- Pilih Maven, isi artifact dengan format indianapolis-[fungsi module] (e.g. indianapolis-web, indianapolis-model, etc)
- Ojo lali jeneng module dikei strip (-) soale defaulte ga pake strip (e.g. indianapolisweb diganti indianapolis-web) biar enak kebacanya
- simpan

### Editing Dependencies
- Setelah bikin module baru, di bagian `pom.xml`-nya module baru, ditambahin `<packaging>jar</packaging` sama dicek apakah module tersebut
bergantung ke module lainnya
Misal module `indianapolis-web` bergantung ke module `indianapolis-persistence` dan `indianapolis-model`, jadinya di `pom.xml`-nya web
dikasih dependencies gini:
```
<dependencies>
  <dependency>
    <groupId>com.gdn</groupId>
    <artifact>indianapolis-web</artifact>
    <version>0.0.1 SNAPSHOT</version>
  </dependency>
  <dependency>
    <groupId>com.gdn</groupId>
    <artifact>indianapolis-persistence</artifact>
    <version>0.0.1 SNAPSHOT</version>
  </dependency>
</dependencies>
```
Ben gampang, ngetik jeneng artifact'e ae (jeneng module) ngko bagian `groupId` karo `version` langsung metu
