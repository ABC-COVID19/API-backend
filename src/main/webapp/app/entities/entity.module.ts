import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'document',
        loadChildren: () => import('./document/document.module').then(m => m.Abccovid19DocumentModule)
      },
      {
        path: 'topic',
        loadChildren: () => import('./topic/topic.module').then(m => m.Abccovid19TopicModule)
      },
      {
        path: 'revision',
        loadChildren: () => import('./revision/revision.module').then(m => m.Abccovid19RevisionModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class Abccovid19EntityModule {}
