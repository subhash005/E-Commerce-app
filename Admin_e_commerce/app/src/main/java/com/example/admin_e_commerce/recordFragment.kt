package com.example.admin_e_commerce

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.File
import java.io.FileOutputStream

class recordFragment : Fragment() {

    private lateinit var totalProductCountText: TextView
    private lateinit var allProductNamesText: TextView
    private lateinit var productSalesSummaryText: TextView
    private lateinit var productSaleproductText: TextView
    private lateinit var bestBuyerText: TextView
    private lateinit var databaseRef: DatabaseReference
    private lateinit var generatePdfBtn: Button
    private var bestBuyerSummary: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_record, container, false)

        totalProductCountText = view.findViewById(R.id.totalProductCountText)
        allProductNamesText = view.findViewById(R.id.allProductNamesText)
        productSalesSummaryText = view.findViewById(R.id.productSalesSummaryText)
        productSaleproductText = view.findViewById(R.id.productSaleproductText)
        bestBuyerText = view.findViewById(R.id.bestbuyer)
        generatePdfBtn = view.findViewById(R.id.generatePdfBtn)

        databaseRef = FirebaseDatabase.getInstance().getReference("Admins").child("AllProducts")

        fetchProductData()
        fetchSalesSummaryFromOrders()
        findBestBuyer()
        setStatusBarColor()

        generatePdfBtn.setOnClickListener {
            generateAndOpenPdf()
        }

        return view
    }

    private fun fetchProductData() {
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var count = 0
                val productTitles = mutableListOf<String>()

                for (productSnapshot in snapshot.children) {
                    val title = productSnapshot.child("productTitle").getValue(String::class.java)
                    if (title != null) {
                        count++
                        productTitles.add(title)
                    }
                }

                totalProductCountText.text = "Total Products: $count"
                allProductNamesText.text = productTitles.joinToString("\n")
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

//    private fun fetchSalesSummaryFromOrders() {
//        val ordersRef = FirebaseDatabase.getInstance().getReference("Admins").child("Orders")
//        val salesMap = mutableMapOf<String, Int>()
//        var totalProfit = 0
//
//        ordersRef.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for (orderSnapshot in snapshot.children) {
//                    val orderListSnapshot = orderSnapshot.child("orderList")
//                    for (productSnapshot in orderListSnapshot.children) {
//                        val title = productSnapshot.child("productTitle").getValue(String::class.java) ?: continue
//                        val count = productSnapshot.child("productCount").getValue(Int::class.java) ?: 0
//                        val priceString = productSnapshot.child("productPrice").getValue(String::class.java) ?: "₹0"
//                        val price = priceString.replace("₹", "").toIntOrNull() ?: 0
//
//                        val totalSale = count * price
//                        totalProfit += totalSale
//
//                        salesMap[title] = salesMap.getOrDefault(title, 0) + totalSale
//                    }
//                }
//
//                val summaryBuilder = StringBuilder()
//                for ((title, totalSales) in salesMap) {
//                    summaryBuilder.append("• $title = ₹$totalSales\n")
//                }
//
//                summaryBuilder.append("\nTotal Profit = ₹$totalProfit")
//                productSalesSummaryText.text = summaryBuilder.toString()
//
//                val highestProduct = salesMap.maxByOrNull { it.value }
//                productSaleproductText.text = if (highestProduct != null) {
//                    "${highestProduct.key} (₹${highestProduct.value})"
//                } else {
//                    "Highest Selling Product: No data"
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(context, "Error loading sales summary: ${error.message}", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
private fun fetchSalesSummaryFromOrders() {
    val ordersRef = FirebaseDatabase.getInstance().getReference("Admins").child("Orders")
    val salesMap = mutableMapOf<String, Int>()
    val quantityMap = mutableMapOf<String, Int>()
    var totalProfit = 0

    ordersRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            for (orderSnapshot in snapshot.children) {
                val orderListSnapshot = orderSnapshot.child("orderList")
                for (productSnapshot in orderListSnapshot.children) {
                    val title = productSnapshot.child("productTitle").getValue(String::class.java) ?: continue
                    val count = productSnapshot.child("productCount").getValue(Int::class.java) ?: 0
                    val priceString = productSnapshot.child("productPrice").getValue(String::class.java) ?: "₹0"
                    val price = priceString.replace("₹", "").toIntOrNull() ?: 0

                    val totalSale = count * price
                    totalProfit += totalSale

                    salesMap[title] = salesMap.getOrDefault(title, 0) + totalSale
                    quantityMap[title] = quantityMap.getOrDefault(title, 0) + count
                }
            }

            val summaryBuilder = StringBuilder()
            for ((title, totalSales) in salesMap) {
                val totalQty = quantityMap[title] ?: 0
                summaryBuilder.append("• $title: ₹$totalSales (Qty Sold: $totalQty)\n")
            }

            summaryBuilder.append("\nTotal Profit = ₹$totalProfit")
            productSalesSummaryText.text = summaryBuilder.toString()

            val highestProduct = salesMap.maxByOrNull { it.value }
            productSaleproductText.text = if (highestProduct != null) {
                val qty = quantityMap[highestProduct.key] ?: 0
                "${highestProduct.key} (₹${highestProduct.value}, Quantity Sold: $qty)"
            } else {
                "Highest Selling Product: No data"
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(context, "Error loading sales summary: ${error.message}", Toast.LENGTH_SHORT).show()
        }
    })
}

    private fun findBestBuyer() {
        val db = FirebaseDatabase.getInstance()
        val ordersRef = db.getReference("Admins").child("Orders")
        val usersRef = db.getReference("AllUsers").child("Users")

        val userTotalMap = mutableMapOf<String, Double>()

        ordersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (orderSnap in snapshot.children) {
                    val userId = orderSnap.child("orderingUserId").getValue(String::class.java) ?: continue
                    val orderListSnap = orderSnap.child("orderList")

                    var totalForOrder = 0.0

                    for (itemSnap in orderListSnap.children) {
                        val priceStr = itemSnap.child("productPrice").getValue(String::class.java)?.replace("₹", "")?.trim()
                        val count = itemSnap.child("productCount").getValue(Int::class.java) ?: 0

                        val price = priceStr?.toDoubleOrNull() ?: 0.0
                        totalForOrder += price * count
                    }

                    userTotalMap[userId] = userTotalMap.getOrDefault(userId, 0.0) + totalForOrder
                }

                val bestBuyerId = userTotalMap.maxByOrNull { it.value }?.key
                val totalSpent = userTotalMap[bestBuyerId] ?: 0.0

                if (bestBuyerId != null) {
                    usersRef.child(bestBuyerId).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(userSnapshot: DataSnapshot) {
                            val phone = userSnapshot.child("userPhoneNumber").getValue(String::class.java) ?: "N/A"
                            val address = userSnapshot.child("userAddress").getValue(String::class.java) ?: "N/A"

                            val summaryText = "\nPhone: $phone\nAddress: $address\nTotal Spent: ₹%.2f".format(totalSpent)
                            bestBuyerText.text = summaryText
                            bestBuyerSummary = summaryText
                        }

                        override fun onCancelled(error: DatabaseError) {}
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun generateAndOpenPdf() {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        val titlePaint = Paint().apply {
            textSize = 20f
            isFakeBoldText = true
        }

        val sectionTitlePaint = Paint().apply {
            textSize = 16f
            isFakeBoldText = true
        }

        val contentPaint = Paint().apply {
            textSize = 14f
        }

        var y = 40

        canvas.drawText("Records Details", 220f, y.toFloat(), titlePaint)
        y += 40

        canvas.drawText(totalProductCountText.text.toString(), 20f, y.toFloat(), sectionTitlePaint)
        y += 30

        canvas.drawText("All Products Names:", 20f, y.toFloat(), sectionTitlePaint)
        y += 25

        val productLines = allProductNamesText.text.split("\n")
        for (line in productLines) {
            canvas.drawText("• $line", 30f, y.toFloat(), contentPaint)
            y += 20
        }

        y += 20
        canvas.drawText("Sales Summary:", 20f, y.toFloat(), sectionTitlePaint)
        y += 25

        val summaryLines = productSalesSummaryText.text.split("\n")
        for (line in summaryLines) {
            canvas.drawText(line, 30f, y.toFloat(), contentPaint)
            y += 20
        }

        y += 20
        canvas.drawText("Top Selling Product:", 20f, y.toFloat(), sectionTitlePaint)
        y += 25
        canvas.drawText(productSaleproductText.text.toString(), 30f, y.toFloat(), contentPaint)

        y += 40
        canvas.drawText("Best Buyer:", 20f, y.toFloat(), sectionTitlePaint)
        y += 25

        val bestBuyerLines = bestBuyerSummary.split("\n")
        for (line in bestBuyerLines) {
            canvas.drawText(line, 30f, y.toFloat(), contentPaint)
            y += 20
        }

        pdfDocument.finishPage(page)

        val fileName = "sales_report.pdf"
        val filePath = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
        pdfDocument.writeTo(FileOutputStream(filePath))
        pdfDocument.close()

        Toast.makeText(requireContext(), "PDF Generated!", Toast.LENGTH_SHORT).show()
        openPdfFile(filePath)
    }

    private fun openPdfFile(file: File) {
        val uri: Uri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            file
        )

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "application/pdf")
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NO_HISTORY

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireContext(), "No PDF viewer found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setStatusBarColor() {
        activity?.window?.apply {
            statusBarColor = ContextCompat.getColor(requireContext(), R.color.blue)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }
}
