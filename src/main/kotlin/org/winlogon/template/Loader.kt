package org.winlogon.template

import io.papermc.paper.plugin.loader.PluginClasspathBuilder
import io.papermc.paper.plugin.loader.PluginLoader
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver

import org.eclipse.aether.artifact.DefaultArtifact
import org.eclipse.aether.graph.Dependency
import org.eclipse.aether.repository.RemoteRepository

class Loader : PluginLoader {
    override fun classloader(classpathBuilder: PluginClasspathBuilder) {
        val resolver = MavenLibraryResolver()

        val repositories = mapOf(
            "central" to MavenLibraryResolver.MAVEN_CENTRAL_DEFAULT_MIRROR,
            "winlogon-libs" to "https://maven.winlogon.org/releases/",
        )

        val dependencies = mapOf(
            "org.winlogon:retrohue" to "0.1.1",
            "org.winlogon:asynccraftr" to "0.1.0",
        )

        repositories.forEach { (name, url) -> 
            resolver.addRepository(
                RemoteRepository.Builder(name, "default", url).build()
            )
        }

        dependencies.forEach { (dependencyPackage, version) -> 
            resolver.addDependency(
                Dependency(DefaultArtifact("$dependencyPackage:$version"), null)
            )
        }

        classpathBuilder.addLibrary(resolver)
    }
}
