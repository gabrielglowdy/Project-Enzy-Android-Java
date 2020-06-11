# Project Enzy Android Java

## Project iseng
Jadi project merupakan project iseng saya, Pada dasarnya akan sistem akan mencari jawaban dari kata yang diinput user dan jika tidak menemukannya sistem meminta user untuk mengajarinya

## Algoritm
Sistem menyimpan "database*" yang berisi key dan result dimana key berisi kata kunci query dan result adalah bagaimana sistem harus menjawabnya

Jadi Secara sederhana, 
- User memasukan sebuah query
- sistem akan mencari key pada database* yang memiliki tingkat kemiripan yang paling tinggi dengan query tersebut (menggunakan equal dan contains)
- lalu Sistem akan menampilkan result sesuai dengan key yang memiliki kemiripan paling tinggi dengan query

### Case 1 (Jawaban tidak sesuai)
- jika tidak sesuai, user bisa kembali membalas NO, WRONG, atau N 
- Kemudian Sistem akan meminta jawaban bagaimana sistem tersebut seharusnya menjawab
- lalu user memasukkan jawaban yang benar
- kemudian Sistem akan menambahkan data tersebut ke database dengan key=query sebelumnya dan result adalah jawaban user tentang bagaimana sistem seharusnya menjawab

### Case 2 (Jawaban tidak ada)
- jika sistem tidak menemukan jawabannya maka sistem akan meminta user untuk memasukkan jawaban yang seharusnya
- kemudian user memasukkan jawaban yang seharusnya 
- kemudian Sistem akan menambahkan data tersebut ke database dengan key=query sebelumnya dan result adalah jawaban user tentang bagaimana sistem seharusnya menjawab

## Kesimpulan 
Kesimpulan semakin banyak knowledges atau data yang tersimpan maka sistem semakin mampu menganalisa jawaban yang benar 
(* Jika algoritmaku tidak salah dan tidak terjadi bug lagi)



