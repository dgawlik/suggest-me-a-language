package org.dgawlik

import org.dgawlik.domain.BinaryField
import org.dgawlik.domain.Feature
import org.dgawlik.domain.Language
import org.dgawlik.parsing.Parser
import org.dgawlik.tree.Id3Tree
import org.dgawlik.tree.TreeNode
import org.http4k.core.Body
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.format.Jackson.auto
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.SunHttp
import org.http4k.server.asServer


const val banner = """ 
=====================================================================================================

  █████████                                                 █████                               
 ███░░░░░███                                               ░░███                                
░███    ░░░  █████ ████  ███████  ███████  ██████   █████  ███████      █████████████    ██████ 
░░█████████ ░░███ ░███  ███░░███ ███░░███ ███░░███ ███░░  ░░░███░      ░░███░░███░░███  ███░░███
 ░░░░░░░░███ ░███ ░███ ░███ ░███░███ ░███░███████ ░░█████   ░███        ░███ ░███ ░███ ░███████ 
 ███    ░███ ░███ ░███ ░███ ░███░███ ░███░███░░░   ░░░░███  ░███ ███    ░███ ░███ ░███ ░███░░░  
░░█████████  ░░████████░░███████░░███████░░██████  ██████   ░░█████     █████░███ █████░░██████ 
 ░░░░░░░░░    ░░░░░░░░  ░░░░░███ ░░░░░███ ░░░░░░  ░░░░░░     ░░░░░     ░░░░░ ░░░ ░░░░░  ░░░░░░  
                        ███ ░███ ███ ░███                                                       
                       ░░██████ ░░██████                                                        
                        ░░░░░░   ░░░░░░                                                         
              ████                                                                              
             ░░███                                                                              
  ██████      ░███   ██████   ████████    ███████ █████ ████  ██████    ███████  ██████         
 ░░░░░███     ░███  ░░░░░███ ░░███░░███  ███░░███░░███ ░███  ░░░░░███  ███░░███ ███░░███        
  ███████     ░███   ███████  ░███ ░███ ░███ ░███ ░███ ░███   ███████ ░███ ░███░███████         
 ███░░███     ░███  ███░░███  ░███ ░███ ░███ ░███ ░███ ░███  ███░░███ ░███ ░███░███░░░          
░░████████    █████░░████████ ████ █████░░███████ ░░████████░░████████░░███████░░██████         
 ░░░░░░░░    ░░░░░  ░░░░░░░░ ░░░░ ░░░░░  ░░░░░███  ░░░░░░░░  ░░░░░░░░  ░░░░░███ ░░░░░░          
                                         ███ ░███                      ███ ░███                 
                                        ░░██████                      ░░██████                  
                                         ░░░░░░                        ░░░░░░              
 
    
=====================================================================================================
"""




fun main() {
    println(banner)

    val port = 8080

    val db = BinaryField::class.java.getResource("/Database.md")!!.readText()
    val index = BinaryField::class.java.getResource("/static/index.html")!!.readText()

    val parser = Parser(db)
    parser.parse()

    val cart = Id3Tree(parser.languages, parser.features)

    val languageLens = Body.auto<Array<Language>>().toLens()
    val featureLens = Body.auto<Array<Feature>>().toLens()
    val treeLens = Body.auto<TreeNode>().toLens()

    routes(
        "/languages" bind Method.GET to {
            Response(OK).with(languageLens of parser.languages)
        },
        "/features" bind Method.GET to {
            Response(OK).with(featureLens of parser.features)
        },
        "/tree" bind Method.GET to {
            Response(OK).with(treeLens of cart.root)
        },
        "/" bind Method.GET to {
            Response(OK).header("Content-Type", "text/html;charset=UTF-8").body(index)
        }
    ).asServer(SunHttp(port)).start()

    println("Listening on http://0.0.0.0:${port}/")
}
